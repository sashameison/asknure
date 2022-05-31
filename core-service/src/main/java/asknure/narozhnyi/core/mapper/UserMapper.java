package asknure.narozhnyi.core.mapper;

import asknure.narozhnyi.core.dto.UserDto;
import asknure.narozhnyi.core.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toDto(User user);
}
