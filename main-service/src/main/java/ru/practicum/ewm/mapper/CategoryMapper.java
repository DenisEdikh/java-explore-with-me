package ru.practicum.ewm.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.model.Category;

import java.util.List;

@Component
public class CategoryMapper {
    public Category toCategory(NewCategoryDto dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getName());

        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
//        if (category == null) {
//            return null;
//        }

        return new CategoryDto(category.getId(), category.getName());
    }

    public List<CategoryDto> toCategoryDto(List<Category> categories) {
//        if (categories == null) {
//            return null;
//        }

        return categories.stream().map(this::toCategoryDto).toList();
    }
}
