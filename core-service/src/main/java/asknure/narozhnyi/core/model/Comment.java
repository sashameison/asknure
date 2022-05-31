package asknure.narozhnyi.core.model;

import java.time.Instant;
import java.util.UUID;

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
public class Comment {
  @Builder.Default
  private String id = UUID.randomUUID().toString();
  private String author;
  private String text;
  private String photo;
  @Builder.Default
  private Instant createdAt = Instant.now();
}
