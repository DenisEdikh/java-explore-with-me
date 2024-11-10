package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.param.AdminRequestParam;
import ru.practicum.ewm.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    @Transactional
    public void delete(Long catId) {
        checkExistenceCat(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto, Long catId) {
        Category cat = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(Category.class, catId));
        cat.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(cat));
    }

    @Override
    public List<CategoryDto> getAll(AdminRequestParam param) {
        Pageable page = PageRequest.of(param.getPage(), param.getSize());
        return categoryMapper.toCategoryDto(categoryRepository.findAll(page).toList());
    }

    @Override
    public CategoryDto getById(Long catId) {
        return categoryMapper.toCategoryDto(categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(Category.class, catId)));
    }

    private void checkExistenceCat(Long catId) {
        log.debug("Start checking contains category with catId {}", catId);
        if (!categoryRepository.existsById(catId)) {
            log.warn("Category with catId {} does not exist", catId);
            throw new NotFoundException(Category.class, catId);
        }
    }
}
