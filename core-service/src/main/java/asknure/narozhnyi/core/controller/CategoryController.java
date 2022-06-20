package asknure.narozhnyi.core.controller;

import asknure.narozhnyi.core.dto.CategoryCreateDto;
import asknure.narozhnyi.core.dto.CategoryDto;
import asknure.narozhnyi.core.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public Page<CategoryDto> findAll(@PageableDefault Pageable pageable) {
    return categoryService.findAllBy(pageable);
  }

  @GetMapping("/{id}")
  public CategoryDto findById(@PathVariable("id") String id) {
    return categoryService.findById(id);
  }

  @PostMapping
  public CategoryDto save(@RequestBody CategoryCreateDto dto) {
    return categoryService.save(dto);
  }

  @DeleteMapping
  public void deleteByName(@RequestParam("name") String name) {
    categoryService.deleteByName(name);
  }
}
