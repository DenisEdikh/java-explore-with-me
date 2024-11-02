package ru.practicum.ewm.stats.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.stats.EndpointHitDto;
import ru.practicum.ewm.stats.model.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointHitMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {

        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setIp(endpointHitDto.getIp());
        endpointHit.setApp(endpointHitDto.getApp());
        endpointHit.setUri(endpointHitDto.getUri());
        endpointHit.setTimestamp(endpointHitDto.getTimestamp());
        return endpointHit;
    }
}
