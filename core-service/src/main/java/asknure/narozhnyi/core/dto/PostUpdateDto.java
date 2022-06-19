package asknure.narozhnyi.core.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostUpdateDto {
  String title;
  String text;
}
