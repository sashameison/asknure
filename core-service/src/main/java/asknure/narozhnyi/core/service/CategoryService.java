package asknure.narozhnyi.core.service;

import java.util.Optional;

import asknure.narozhnyi.core.dto.CategoryCreateDto;
import asknure.narozhnyi.core.dto.CategoryDto;
import asknure.narozhnyi.core.exceptions.BadRequest;
import asknure.narozhnyi.core.exceptions.NotFoundException;
import asknure.narozhnyi.core.mapper.CategoryMapper;
import asknure.narozhnyi.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  public Page<CategoryDto> findAllBy(Pageable pageable) {
    return categoryRepository.findCategoryBy(pageable)
        .map(categoryMapper::toDto);
  }

  public CategoryDto findById(String id) {
    return categoryRepository.findCategoryById(id)
        .map(categoryMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public CategoryDto save(CategoryCreateDto dto) {
    boolean isCategoryExists = categoryRepository.findCategoryByNameIgnoreCase(dto.getName())
        .isPresent();
    if (isCategoryExists) {
      throw new BadRequest();
    }

    return Optional.of(dto)
        .map(categoryMapper::toEntity)
        .map(categoryRepository::save)
        .map(categoryMapper::toDto)
        .orElseThrow(BadRequest::new);
  }

  public void deleteByName(String name) {
    categoryRepository.deleteCategoryByName(name);
  }
}
