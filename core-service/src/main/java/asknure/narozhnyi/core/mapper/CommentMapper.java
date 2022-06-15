package asknure.narozhnyi.core.mapper;

import asknure.narozhnyi.core.dto.CommentCreateDto;
import asknure.narozhnyi.core.model.Comment;
import org.mapstruct.Mapper;

@Mapper
public interface CommentMapper {
  Comment toEntity(CommentCreateDto commentDto);
}
