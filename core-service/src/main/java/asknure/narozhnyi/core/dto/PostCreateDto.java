package asknure.narozhnyi.core.dto;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostCreateDto {

  @NotNull
  @Size(min = 2)
  private String title;
  private String text;
  private String photo;
  @Builder.Default
  private List<CommentCreateDto> comments = new ArrayList<>();
  @Builder.Default
  private List<String> categories = new ArrayList<>();
  @Builder.Default
  private List<String> files = new ArrayList<>();
}
