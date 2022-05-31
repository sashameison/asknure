package asknure.narozhnyi.core.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDtoResponse {

  private String id;
  private String title;
  private String createdBy;
  private String color;
  private String photo;
  private List<String> categories;
  private Instant createdAt;
  private Instant updatedAt;
  private Integer answersCount;
}
