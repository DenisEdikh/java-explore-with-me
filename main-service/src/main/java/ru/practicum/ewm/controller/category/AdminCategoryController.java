package ru.practicum.ewm.controller.category;

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
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.log.Log;
import ru.practicum.ewm.service.category.CategoryService;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final Log logger;
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@Valid @RequestBody CategoryDto categoryDto, HttpServletRequest req) {
        logger.startLog(req);
        final CategoryDto storedCategory = categoryService.create(categoryDto);
        logger.finishLog(req);
        return storedCategory;
    }

    @DeleteMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "catId") Long catId, HttpServletRequest req) {
        logger.startLog(req);
        categoryService.delete(catId);
        logger.finishLog(req);
    }

    @PatchMapping(path = "/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto,
                              @PathVariable(value = "catId") Long catId,
                              HttpServletRequest req) {
        logger.startLog(req);
        final CategoryDto catDto = categoryService.update(categoryDto, catId);
        logger.finishLog(req);
        return catDto;
    }
}
