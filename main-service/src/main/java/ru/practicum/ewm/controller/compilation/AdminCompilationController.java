package ru.practicum.ewm.controller.compilation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.service.compilation.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final Log logger;
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid NewCompilationDto dto,
                                 HttpServletRequest req) {
        logger.startLog(req);
        final CompilationDto compilation = compilationService.create(dto);
        logger.finishLog(req);
        return compilation;
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "compId") Long compId, HttpServletRequest req) {
        logger.startLog(req);
        compilationService.delete(compId);
        logger.finishLog(req);
    }

    @PatchMapping(path = "/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@RequestBody @Valid UpdateCompilationRequest request,
                                 @PathVariable(value = "compId") Long compId,
                                 HttpServletRequest req) {
        logger.startLog(req);
        final CompilationDto compilation = compilationService.update(request, compId);
        logger.finishLog(req);
        return compilation;
    }
}
