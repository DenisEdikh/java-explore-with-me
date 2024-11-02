package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.ViewStatDto;

import java.util.List;

public interface StatService {
    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatDto> getViewDto(String start, String end, List<String> uris, boolean unique);
}
