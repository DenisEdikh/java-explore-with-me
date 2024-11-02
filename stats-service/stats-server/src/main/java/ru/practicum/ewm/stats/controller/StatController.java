package ru.practicum.ewm.stats.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.service.StatService;
import ru.practicum.ewm.stats.ViewStatDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StatController {
    private final StatService statService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@Valid @RequestBody EndpointHitDto endpointHitDto, HttpServletRequest req) {
        startLog(req);
        statService.saveHit(endpointHitDto);
        finishLog(req);
    }

    @GetMapping(path = "/stats")
    public List<ViewStatDto> getViewStats(@RequestParam(value = "start") String start,
                                          @RequestParam(value = "end") String end,
                                          @RequestParam(value = "uris", required = false) List<String> uris,
                                          @RequestParam(value = "unique", defaultValue = "false") boolean unique,
                                          HttpServletRequest req) {
        startLog(req);
        final List<ViewStatDto> dtos = statService.getViewDto(start, end, uris, unique);
        finishLog(req);
        return dtos;
    }


    private void startLog(HttpServletRequest req) {
        String method = req.getMethod();
        String query = req.getQueryString();
        log.info("Start logging: method: {}, query {}", method, query);
    }

    private void finishLog(HttpServletRequest req) {
        String method = req.getMethod();
        String query = req.getQueryString();
        log.info("Finish logging: method {}, query {}", method, query);
    }
}
