package asknure.narozhnyi.core.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "posts")
public class Post extends AuditingEntity {
  @Id
  private String id;
  private String title;
  private String text;
  private String color;
  private String photo;
  private List<String> files;
  private List<String> categories;
  private List<Comment> comments;
  private String userId;
}
