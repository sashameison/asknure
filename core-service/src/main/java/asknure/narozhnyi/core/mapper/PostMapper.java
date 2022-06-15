package asknure.narozhnyi.core.mapper;

import asknure.narozhnyi.core.dto.PostCreateDto;
import asknure.narozhnyi.core.dto.PostDto;
import asknure.narozhnyi.core.dto.PostDtoResponse;
import asknure.narozhnyi.core.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PostMapper {
  PostDto toDto(Post post);

  @Mapping(target = "answersCount", expression = "java(post.getComments().size())")
  PostDtoResponse toResponseDto(Post post);

  Post toEntity(PostCreateDto postDto);
}
