package ru.practicum.ewm.service.request;

import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getAll(Long userId);

    ParticipationRequestDto create(Long requesterId, Long eventId);

    ParticipationRequestDto update(Long requesterId, Long requestId);
}
