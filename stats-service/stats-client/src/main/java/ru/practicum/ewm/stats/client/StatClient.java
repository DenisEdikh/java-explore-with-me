package ru.practicum.ewm.stats.client;

import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.ViewStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatClient {
    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatDto> getViewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
