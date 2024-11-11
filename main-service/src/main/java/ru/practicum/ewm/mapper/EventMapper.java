package ru.practicum.ewm.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public Event toEvent(NewEventDto dto, User initiator, Category category) {
        if (dto == null) {
            return null;
        }

        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .category(category)
                .eventDate(dto.getEventDate())
                .initiator(initiator)
                .location(dto.getLocation())
                .paid(dto.isPaid())
                .requestModeration(dto.isRequestModeration())
                .participantLimit(dto.getParticipantLimit())
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .publishedOn(event.getPublishedOn())
                .createdOn(event.getCreatedOn())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .requestModeration(event.isRequestModeration())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public List<EventFullDto> toEventFullDto(List<Event> events) {
        return events.stream().map(this::toEventFullDto).collect(Collectors.toList());
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public List<EventShortDto> toEventShortDto(List<Event> events) {
        return events.stream().map(this::toEventShortDto).collect(Collectors.toList());
    }
}
