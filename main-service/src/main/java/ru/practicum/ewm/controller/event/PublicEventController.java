package ru.practicum.ewm.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.param.PublicRequestParam;
import ru.practicum.ewm.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final Log logger;
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAll(@Valid PublicRequestParam param, HttpServletRequest req) {
        logger.startLog(req);
        final List<EventShortDto> events = eventService.getAll(param, req);
        logger.finishLog(req);
        return events;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long id, HttpServletRequest req) {
        logger.startLog(req);
        final EventFullDto event = eventService.getById(id, req);
        logger.finishLog(req);
        return event;
    }
}
