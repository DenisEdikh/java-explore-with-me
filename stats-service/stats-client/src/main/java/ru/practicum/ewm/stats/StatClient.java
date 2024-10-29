package ru.practicum.ewm.stats;

import java.util.List;

public interface StatClient {
    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatDto> getViewStats(String start, String end, List<String> uris, boolean unique);
}
