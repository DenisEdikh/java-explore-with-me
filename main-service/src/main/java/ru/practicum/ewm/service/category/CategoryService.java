package ru.practicum.ewm.service.category;

import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.param.AdminPageParam;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    void delete(Long catId);

    CategoryDto update(CategoryDto categoryDto, Long catId);

    List<CategoryDto> getAll(AdminPageParam param);

    CategoryDto getById(Long catId);
}
