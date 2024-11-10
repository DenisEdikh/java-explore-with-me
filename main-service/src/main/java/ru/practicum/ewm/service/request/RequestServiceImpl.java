package ru.practicum.ewm.service.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getAll(Long requesterId) {
        checkExistenceUser(requesterId);
        final List<Request> requests = requestRepository.findByRequesterId(requesterId);
        return requestMapper.toParticipationRequestDto(requests);
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long requesterId, Long eventId) {
        log.debug("Start checking contains user with userId {}", requesterId);
        final User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(User.class, requesterId));
        log.debug("Start checking contains event with eventId {}", eventId);
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class, eventId));
        Optional<Request> storedRequest = requestRepository.findByRequesterIdAndEventId(requesterId, eventId);

        if (storedRequest.isPresent()) {
            throw new ConflictException(Request.class, storedRequest.get().getId());
        }
        if (event.getInitiator().getId().equals(requesterId)
            || event.getState() != EventState.PUBLISHED
            || (event.getParticipantLimit() == event.getConfirmedRequests() && event.getParticipantLimit() > 0)) {
            throw new ConflictException(Request.class, requesterId, eventId);
        }

        final Request request = requestMapper.toRequest(requester, event);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto update(Long requesterId, Long requestId) {
        checkExistenceUser(requesterId);
        final Request request = requestRepository
                .findById(requestId).orElseThrow(() -> new NotFoundException(Request.class, requestId));
        request.setStatus(RequestState.CANCELED);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    private void checkExistenceUser(Long userId) {
        log.debug("Start checking contains user with userId {}", userId);
        if (!userRepository.existsById(userId)) {
            log.warn("User with userId {} does not exist", userId);
            throw new NotFoundException(User.class, userId);
        }
    }
}
