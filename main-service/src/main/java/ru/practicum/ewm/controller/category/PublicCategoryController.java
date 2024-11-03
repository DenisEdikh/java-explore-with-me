package ru.practicum.ewm.controller.category;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.param.AdminPageParam;
import ru.practicum.ewm.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final Log logger;
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@Valid AdminPageParam param, HttpServletRequest req) {
        logger.startLog(req);
        final List<CategoryDto> catDto = categoryService.getAll(param);
        logger.finishLog(req);
        return catDto;
    }

    @GetMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable(value = "catId") Long catId, HttpServletRequest req) {
        logger.startLog(req);
        final CategoryDto catDto = categoryService.getById(catId);
        logger.finishLog(req);
        return catDto;
    }
}
