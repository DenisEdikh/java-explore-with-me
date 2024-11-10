package ru.practicum.ewm.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.param.PrivateRequestParam;
import ru.practicum.ewm.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final Log logger;
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@RequestBody @Valid NewEventDto newEventDto,
                             @PathVariable(value = "userId") Long initiatorId,
                             HttpServletRequest req) {
        logger.startLog(req);
        final EventFullDto event = eventService.save(newEventDto, initiatorId);
        logger.finishLog(req);
        return event;
    }

    @PatchMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                               @PathVariable(value = "userId") Long initiatorId,
                               @PathVariable(value = "eventId") Long eventId,
                               HttpServletRequest req) {
        logger.startLog(req);
        final EventFullDto event = eventService.update(updateEventUserRequest, initiatorId, eventId);
        logger.finishLog(req);
        return event;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAll(@PathVariable(value = "userId") Long initiatorId,
                                      @Valid PrivateRequestParam param,
                                      HttpServletRequest req) {
        logger.startLog(req);
        final List<EventShortDto> events = eventService.getAll(param, initiatorId);
        logger.finishLog(req);
        return events;
    }

    @GetMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable(value = "userId") Long initiatorId,
                                @PathVariable(value = "eventId") Long eventId,
                                HttpServletRequest req) {
        logger.startLog(req);
        final EventFullDto event = eventService.getById(initiatorId, eventId);
        logger.finishLog(req);
        return event;
    }

    @PatchMapping(path = "/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult update(@RequestBody @Valid EventRequestStatusUpdateRequest request,
                                                 @PathVariable(value = "userId") Long initiatorId,
                                                 @PathVariable(value = "eventId") Long eventId,
                                                 HttpServletRequest req) {
        logger.startLog(req);
        final EventRequestStatusUpdateResult result = eventService.update(request, initiatorId, eventId);
        logger.finishLog(req);
        return result;
    }

    @GetMapping(path = "/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(@PathVariable(value = "userId") Long initiatorId,
                                                @PathVariable(value = "eventId") Long eventId,
                                                HttpServletRequest req) {
        logger.startLog(req);
        final List<ParticipationRequestDto> requests = eventService.getAll(initiatorId, eventId);
        logger.finishLog(req);
        return requests;
    }
}
