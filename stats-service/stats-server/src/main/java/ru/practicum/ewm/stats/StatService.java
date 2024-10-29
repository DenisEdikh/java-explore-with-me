package ru.practicum.ewm.stats;

import java.util.List;

public interface StatService {
    void saveHit(EndpointHitDto endpointHitDto);

    List<ViewStatDto> getViewDto(String start, String end, List<String> uris, boolean unique);
}
