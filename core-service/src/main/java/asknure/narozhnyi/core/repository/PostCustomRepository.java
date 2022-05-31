package asknure.narozhnyi.core.repository;

import asknure.narozhnyi.core.dto.PostSearchParam;
import asknure.narozhnyi.core.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {
  Page<Post> findPostBy(Pageable pageable, PostSearchParam postSearchParam);
}
