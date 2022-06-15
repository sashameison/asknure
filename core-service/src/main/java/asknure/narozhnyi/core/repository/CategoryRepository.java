package asknure.narozhnyi.core.repository;

import java.util.Optional;

import asknure.narozhnyi.core.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

  Page<Category> findCategoryBy(Pageable pageable);

  Optional<Category> findCategoryById(String id);

  Optional<Category> findCategoryByName(String name);
}
