package asknure.narozhnyi.core.repository;

import java.util.List;
import java.util.Optional;

import asknure.narozhnyi.core.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post, String>, PostCustomRepository {
  Optional<Post> findPostById(String id);

  Page<Post> findPostByUserId(Pageable pageable, String userId);

  Page<Post> findPostByCreatedBy(Pageable pageable, String createdBy);

  List<Post> findPostByCommentsAuthor(String author);
}
