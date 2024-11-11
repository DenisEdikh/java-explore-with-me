package ru.practicum.ewm.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.param.AdminRequestParam;
import ru.practicum.ewm.param.PrivateRequestParam;
import ru.practicum.ewm.param.PublicRequestParam;

import java.util.List;

public interface EventService {
    EventFullDto save(NewEventDto newEventDto, Long initiatorId);

    EventFullDto update(UpdateEventUserRequest request, Long initiatorId, Long eventId);

    EventFullDto update(UpdateEventAdminRequest request, Long eventId);

    EventRequestStatusUpdateResult update(EventRequestStatusUpdateRequest request,
                                          Long initiatorId,
                                          Long eventId);

    List<EventShortDto> getAll(PrivateRequestParam param, Long initiatorId);

    List<EventFullDto> getAll(AdminRequestParam param);

    List<EventShortDto> getAll(PublicRequestParam param, HttpServletRequest req);

    EventFullDto getById(Long initiatorId, Long eventId);

    EventFullDto getById(Long eventId, HttpServletRequest req);

    List<ParticipationRequestDto> getAll(Long initiatorId, Long eventId);
}
