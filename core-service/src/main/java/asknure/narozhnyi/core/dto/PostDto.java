package asknure.narozhnyi.core.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import asknure.narozhnyi.core.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Builder
public class PostDto {

  private String id;
  private String title;
  private String createdBy;
  private String color;
  private String photo;
  @Builder.Default
  private List<String> files = new ArrayList<>();
  private Instant createdAt;
  private Instant updatedAt;
  @Builder.Default
  private List<String> categories = new ArrayList<>();
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();
}
