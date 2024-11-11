package ru.practicum.ewm.service.event;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventSortType;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.EventStateActionAdmin;
import ru.practicum.ewm.model.EventStateActionUser;
import ru.practicum.ewm.model.QEvent;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.param.AbstractRequestParam;
import ru.practicum.ewm.param.AdminRequestParam;
import ru.practicum.ewm.param.PrivateRequestParam;
import ru.practicum.ewm.param.PublicRequestParam;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.ViewStatDto;
import ru.practicum.ewm.stats.client.StatClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final String app;
    private final StatClient statClient;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Autowired
    public EventServiceImpl(@Value("${application.name}") String app,
                            StatClient statClient,
                            EventMapper eventMapper,
                            EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            RequestRepository requestRepository,
                            RequestMapper requestMapper) {
        this.app = app;
        this.statClient = statClient;
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    @Transactional
    @Override
    public EventFullDto save(NewEventDto newEventDto, Long initiatorId) {
        final User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class, initiatorId));
        final Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(Category.class, newEventDto.getCategory()));
        final Event fullEvent = eventMapper.toEvent(newEventDto, initiator, category);
        final Event storedEvent = eventRepository.save(fullEvent);

        return eventMapper.toEventFullDto(storedEvent);
    }

    @Transactional
    @Override
    public EventFullDto update(UpdateEventUserRequest request, Long initiatorId, Long eventId) {
        checkExistenceUser(initiatorId);
        final Event updatedEvent = updateNonNullFields(request, eventId);
        return eventMapper.toEventFullDto(eventRepository.save(updatedEvent));
    }

    @Transactional
    @Override
    public EventFullDto update(UpdateEventAdminRequest request, Long eventId) {
        final Event updatedEvent = updateNonNullFields(request, eventId);
        return eventMapper.toEventFullDto(eventRepository.save(updatedEvent));
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult update(EventRequestStatusUpdateRequest request,
                                                 Long initiatorId,
                                                 Long eventId) {
        checkExistenceUser(initiatorId);
        final Event storedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));

        if (storedEvent.getParticipantLimit() == storedEvent.getConfirmedRequests()) {
            throw new ConflictException(Event.class, eventId);
        }
        final List<Request> analyzedRequest = analyzeRequest(request, storedEvent);
        eventRepository.save(storedEvent);
        return getRequestStatusResult(analyzedRequest);
    }

    @Override
    public List<EventShortDto> getAll(PrivateRequestParam param, Long initiatorId) {
        checkExistenceUser(initiatorId);
        final Pageable page = PageRequest.of(param.getPage(), param.getSize());
        final Predicate byUserId = QEvent.event.initiator.id.eq(initiatorId);
        final List<Event> storedEvents = eventRepository.findAll(byUserId, page).toList();
        getViewsStats(storedEvents);
        return eventMapper.toEventShortDto(storedEvents);
    }

    @Override
    public List<EventFullDto> getAll(AdminRequestParam param) {
        final Pageable page = PageRequest.of(param.getPage(), param.getSize());
        final Predicate byParam = searchByParam(param);
        final List<Event> storedEvents = eventRepository.findAll(byParam, page).toList();
        getViewsStats(storedEvents);
        return eventMapper.toEventFullDto(storedEvents);
    }

    @Override
    public List<EventShortDto> getAll(PublicRequestParam param, HttpServletRequest req) {
        final LocalDateTime start = param.getRangeStart();
        final LocalDateTime end = param.getRangeEnd();

        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Дата некорректная");
        }
        final Pageable page = PageRequest.of(param.getPage(), param.getSize());
        final Predicate byParam = searchByParam(param);
        final List<Event> storedEvents = eventRepository.findAll(byParam, page).toList();
        saveHit(req);
        getViewsStats(storedEvents);
        final List<EventShortDto> fullEvents = eventMapper.toEventShortDto(storedEvents);

        if (EventSortType.VIEWS == param.getSort()) {
            fullEvents.sort(Comparator.comparing(EventShortDto::getViews));
        } else if (EventSortType.EVENT_DATE == param.getSort()) {
            fullEvents.sort(Comparator.comparing(EventShortDto::getEventDate));
        }
        return fullEvents;
    }

    @Override
    public EventFullDto getById(Long initiatorId, Long eventId) {
        checkExistenceUser(initiatorId);
        final Event savedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest req) {
        final Event savedEvent = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        saveHit(req);
        getViewsStats(savedEvent);
        return eventMapper.toEventFullDto(savedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getAll(Long initiatorId, Long eventId) {
        checkExistenceUser(initiatorId);
        final List<Request> requests = requestRepository.findByEventId(eventId);
        return requestMapper.toParticipationRequestDto(requests);
    }

    private Predicate searchByParam(AbstractRequestParam param) {
        final QEvent qEvent = QEvent.event;
        final BooleanBuilder byAll = new BooleanBuilder();

        if (param instanceof PublicRequestParam paramPublic) {
            LocalDateTime start = paramPublic.getRangeStart();
            LocalDateTime end = paramPublic.getRangeEnd();

            byAll.and(qEvent.state.eq(EventState.PUBLISHED));
            Optional.ofNullable(paramPublic.getText()).ifPresent(t -> byAll
                    .and(qEvent.annotation.likeIgnoreCase(t))
                    .or(qEvent.description.likeIgnoreCase(paramPublic.getText())));
            Optional.ofNullable(paramPublic.getCategories()).ifPresent(c -> byAll.and(qEvent.category.id.in(c)));
            Optional.ofNullable(start).ifPresentOrElse(
                    s -> byAll.and(qEvent.eventDate.after(s)),
                    () -> byAll.and(qEvent.eventDate.after(LocalDateTime.now()))
            );
            Optional.ofNullable(end).ifPresent(e -> byAll.and(qEvent.eventDate.before(e)));
            Optional.ofNullable(paramPublic.getPaid()).ifPresent(p -> byAll.and(qEvent.paid.eq(p)));
            Optional.ofNullable(paramPublic.getOnlyAvailable()).ifPresentOrElse(
                    a -> byAll.and(qEvent.participantLimit.gt(qEvent.confirmedRequests)),
                    () -> byAll.and(qEvent.participantLimit.goe(qEvent.confirmedRequests))
            );
        }

        if (param instanceof AdminRequestParam paramAdmin) {
            Optional.ofNullable(paramAdmin.getUsers()).ifPresent(u -> byAll.and(qEvent.initiator.id.in(u)));
            Optional.ofNullable(paramAdmin.getStates()).ifPresent(s -> byAll.and(qEvent.state.in(s)));
            Optional.ofNullable(paramAdmin.getCategories()).ifPresent(c -> byAll.and(qEvent.category.id.in(c)));
            Optional.ofNullable(paramAdmin.getRangeStart()).ifPresent(s -> byAll.and(qEvent.eventDate.after(s)));
            Optional.ofNullable(paramAdmin.getRangeEnd()).ifPresent(e -> byAll.and(qEvent.eventDate.before(e)));
        }
        return byAll;
    }

    private List<Request> analyzeRequest(EventRequestStatusUpdateRequest request, Event event) {
        final List<Request> storedRequest = requestRepository.findByIdIn(request.getRequestIds());

        storedRequest.stream()
                .filter(r -> {
                    if (r.getStatus() != RequestState.PENDING) {
                        throw new ConflictException(Request.class, r.getId());
                    } else {
                        return true;
                    }
                }).forEach(r -> {
                    if (event.getParticipantLimit() == event.getConfirmedRequests()
                        || RequestState.REJECTED == request.getStatus()) {
                        r.setStatus(RequestState.REJECTED);
                    } else {
                        r.setStatus(RequestState.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    }
                });
        return requestRepository.saveAll(storedRequest);
    }

    private EventRequestStatusUpdateResult getRequestStatusResult(List<Request> requests) {
        final EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        requests.stream()
                .map(requestMapper::toParticipationRequestDto)
                .forEach(r -> {
                    if (r.getStatus() == RequestState.CONFIRMED) {
                        result.addConfirmedRequests(r);
                    } else if (r.getStatus() == RequestState.REJECTED) {
                        result.addRejectedRequests(r);
                    }
                });
        return result;
    }

    private Event updateNonNullFields(UpdateEventRequest source, Long eventId) {
        final Event storedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        if (storedEvent.getState() == EventState.PUBLISHED) {
            throw new ConflictException(Event.class, eventId);
        }

        if (source instanceof UpdateEventUserRequest) {
            updateForUser((UpdateEventUserRequest) source, storedEvent);
        } else if (source instanceof UpdateEventAdminRequest) {
            updateForAdmin((UpdateEventAdminRequest) source, storedEvent);
        }
        return storedEvent;
    }

    private void updateForUser(UpdateEventUserRequest source, Event event) {

        if (EventStateActionUser.CANCEL_REVIEW == source.getStateAction()) {
            event.setState(EventState.CANCELED);
            return;
        }

        if (EventState.CANCELED == event.getState()) {
            event.setState(EventState.PENDING);
        }
        updateCommonFields(source, event);
    }

    private void updateForAdmin(UpdateEventAdminRequest source, Event event) {
        if (EventState.CANCELED == event.getState()
            || (event.getPublishedOn() != null
                && source.getEventDate() != null
                && !source.getEventDate().isAfter(event.getPublishedOn().plusHours(1)))) {
            throw new ConflictException(Event.class, event.getId());
        }

        if (EventStateActionAdmin.REJECT_EVENT == source.getStateAction()) {
            event.setState(EventState.CANCELED);
            return;
        } else if (EventStateActionAdmin.PUBLISH_EVENT == source.getStateAction()) {
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        }
        updateCommonFields(source, event);
    }

    private void updateCommonFields(UpdateEventRequest request, Event storedEvent) {
        Optional.ofNullable(request.getAnnotation()).ifPresent(storedEvent::setAnnotation);
        Optional.ofNullable(request.getDescription()).ifPresent(storedEvent::setDescription);
        Optional.ofNullable(request.getEventDate()).ifPresent(storedEvent::setEventDate);
        Optional.ofNullable(request.getLocation()).ifPresent(storedEvent::setLocation);
        Optional.ofNullable(request.getPaid()).ifPresent(storedEvent::setPaid);
        Optional.ofNullable(request.getParticipantLimit()).ifPresent(storedEvent::setParticipantLimit);
        Optional.ofNullable(request.getRequestModeration()).ifPresent(storedEvent::setRequestModeration);
        Optional.ofNullable(request.getTitle()).ifPresent(storedEvent::setTitle);
        Optional.ofNullable(request.getCategory()).ifPresent(c -> {
            Category cat = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new NotFoundException(Category.class, request.getCategory()));
            storedEvent.setCategory(cat);
        });
    }

    private void saveHit(HttpServletRequest req) {
        final EndpointHitDto hit = new EndpointHitDto(app, req.getRequestURI(), req.getRemoteAddr(), LocalDateTime.now());
        statClient.saveHit(hit);
    }

    private void getViewsStats(Event event) {
        getViewsStats(List.of(event));
    }

    private void getViewsStats(List<Event> events) {
        final LocalDateTime start = events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(10));
        final LocalDateTime end = LocalDateTime.now();

        final List<String> uris = events.stream().map(e -> "/events/%d".formatted(e.getId())).toList();
        final Map<String, Long> stats = statClient.getViewStats(start, end, uris, true)
                .stream()
                .collect(Collectors.toMap(ViewStatDto::getUri, ViewStatDto::getHits));
        events.forEach(e -> e.setViews(stats.get("/events/%d".formatted(e.getId()))));
    }

    private void checkExistenceUser(Long userId) {
        log.debug("Start checking contains user with userId {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User with userId {} does not exist", userId);
            throw new NotFoundException(User.class, userId);
        }
    }
}
