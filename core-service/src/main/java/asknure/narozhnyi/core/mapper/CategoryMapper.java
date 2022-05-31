package asknure.narozhnyi.core.mapper;

import asknure.narozhnyi.core.dto.CategoryCreateDto;
import asknure.narozhnyi.core.dto.CategoryDto;
import asknure.narozhnyi.core.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
  CategoryDto toDto(Category categoryDto);

  Category toEntity(CategoryCreateDto categoryCreateDto);
}
