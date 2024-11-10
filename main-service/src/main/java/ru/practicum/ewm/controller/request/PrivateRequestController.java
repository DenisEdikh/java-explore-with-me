package ru.practicum.ewm.controller.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.service.request.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final Log logger;
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(@PathVariable(value = "userId") Long requesterId,
                                                HttpServletRequest req) {
        logger.startLog(req);
        final List<ParticipationRequestDto> requests = requestService.getAll(requesterId);
        logger.finishLog(req);
        return requests;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable(value = "userId") Long requesterId,
                                          @RequestParam(value = "eventId") Long eventsId,
                                          HttpServletRequest req) {
        logger.startLog(req);
        final ParticipationRequestDto request = requestService.create(requesterId, eventsId);
        logger.finishLog(req);
        return request;
    }

    @PatchMapping(path = "/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto update(@PathVariable(value = "userId") Long requesterId,
                                          @PathVariable(value = "requestId") Long requestId,
                                          HttpServletRequest req) {
        logger.startLog(req);
        final ParticipationRequestDto request = requestService.update(requesterId, requestId);
        logger.finishLog(req);
        return request;
    }
}
