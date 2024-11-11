package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;

import java.util.List;

@Component
public class RequestMapper {
    public Request toRequest(User requester, Event event) {
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        return request;
    }

    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests.stream().map((this::toParticipationRequestDto)).toList();
    }
}
