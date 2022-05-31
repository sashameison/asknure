package asknure.narozhnyi.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

  private String id;
  private String username;
  private String email;
  private String avatar;
  private Long postCount;
  private boolean isFirstSignIn;
}
