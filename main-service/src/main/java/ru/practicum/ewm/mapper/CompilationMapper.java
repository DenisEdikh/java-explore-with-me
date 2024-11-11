package ru.practicum.ewm.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto dto, List<Event> events) {
        Compilation c = new Compilation();
        c.setTitle(dto.getTitle());
        c.setPinned(dto.getPinned());
        c.getEvents().addAll(events);

        return c;
    }

    public CompilationDto toDto(Compilation c) {
        CompilationDto dto = new CompilationDto();
        dto.setId(c.getId());
        dto.setTitle(c.getTitle());
        dto.setPinned(c.getPinned());
        dto.setEvents(eventMapper.toEventShortDto(new ArrayList<>(c.getEvents())));

        return dto;
    }

    public List<CompilationDto> toDto(List<Compilation> comps) {
        return comps.stream().map(this::toDto).toList();
    }
}
