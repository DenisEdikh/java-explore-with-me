package ru.practicum.ewm.service.category;

import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.param.AdminRequestParam;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(Long catId);

    CategoryDto update(CategoryDto categoryDto, Long catId);

    List<CategoryDto> getAll(AdminRequestParam param);

    CategoryDto getById(Long catId);
}
