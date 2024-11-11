package ru.practicum.ewm.service.compilation;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.param.PublicRequestParam;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto dto);

    void delete(Long compId);

    CompilationDto update(UpdateCompilationRequest request, Long compId);

    List<CompilationDto> getAll(PublicRequestParam param);

    CompilationDto getById(Long compId);
}
