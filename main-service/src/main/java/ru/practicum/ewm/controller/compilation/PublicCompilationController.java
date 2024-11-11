package ru.practicum.ewm.controller.compilation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.param.PublicRequestParam;
import ru.practicum.ewm.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final Log logger;
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@Valid PublicRequestParam param, HttpServletRequest req) {
        logger.startLog(req);
        final List<CompilationDto> comps = compilationService.getAll(param);
        logger.finishLog(req);
        return comps;
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable(value = "compId") Long compId, HttpServletRequest req) {
        logger.startLog(req);
        final CompilationDto comp = compilationService.getById(compId);
        logger.finishLog(req);
        return comp;
    }

}

