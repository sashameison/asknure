package asknure.narozhnyi.core.util;

import java.util.List;

import asknure.narozhnyi.core.model.Post;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StubUtils {

  public static Post stubPost(String title) {
    return Post.builder()
        .title(title)
        .comments(List.of())
        .files(List.of())
        .categories(List.of())
        .build();
  }
}
