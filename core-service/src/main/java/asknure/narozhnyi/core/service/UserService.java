package asknure.narozhnyi.core.service;

import java.util.List;
import java.util.Optional;

import asknure.narozhnyi.core.dto.UserDto;
import asknure.narozhnyi.core.dto.UserUpdateDto;
import asknure.narozhnyi.core.exceptions.NotFoundException;
import asknure.narozhnyi.core.mapper.UserMapper;
import asknure.narozhnyi.core.model.Post;
import asknure.narozhnyi.core.model.User;
import asknure.narozhnyi.core.repository.PostRepository;
import asknure.narozhnyi.core.repository.UserRepository;
import asknure.narozhnyi.core.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final UserMapper userMapper;

  public Page<UserDto> findAll(Pageable pageable) {
    return userRepository.findAllBy(pageable)
        .map(userMapper::toDto);
  }

  public UserDto findById(String userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public UserDto findByName(String name) {
    return userRepository.findUserByUsername(name)
        .map(userMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public UserDto findByEmail(String email) {
    return userRepository.findUserByEmail(email)
        .map(userMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public UserDto obtainNewUser() {
    var jwt = AuthUtil.getJwt();
    return userRepository.findUserByEmail(jwt.getClaimAsString("email"))
        .map(userMapper::toDto)
        .map(dto -> updateIsFirstSignInFlag(dto, false))
        .orElseGet(() -> buildNewUser(jwt));
  }

  public UserDto updateAvatar(String userId, UserUpdateDto dto) {
    var user = userRepository.findUserById(userId)
        .orElseThrow(NotFoundException::new);

    Optional.ofNullable(dto.getUsername())
        .ifPresent(username -> updatePostsCreator(user, username));
    Optional.ofNullable(dto.getAvatar())
        .ifPresent(user::setAvatar);

    userRepository.save(user);
    return userMapper.toDto(user);
  }

  public void updateUserPostsCount() {
    userRepository.findUserByEmail(AuthUtil.getEmail())
        .ifPresent(user -> {
          user.setPostCount(user.getPostCount() + 1L);
          userRepository.save(user);
        });
  }

  public void deleteById(String id) {
    userRepository.deleteById(id);
  }

  private void updatePostsCreator(User user, String newUsername) {
    var posts = postRepository.findPostByUserId(Pageable.unpaged(), user.getId());
    posts.forEach(post -> post.setCreatedBy(newUsername));
    postRepository.saveAll(posts);
    var postByCommentsAuthor = postRepository.findPostByCommentsAuthor(user.getUsername());
    postByCommentsAuthor.stream()
        .map(Post::getComments)
        .flatMap(List::stream)
        .filter(comment -> user.getUsername().equals(comment.getAuthor()))
        .forEach(comment -> comment.setAuthor(newUsername));
    postRepository.saveAll(postByCommentsAuthor);
    user.setUsername(newUsername);
  }

  private UserDto buildNewUser(Jwt jwt) {
    var user = User.builder()
        .email(jwt.getClaimAsString("email"))
        .username(jwt.getClaimAsString("name"))
        .avatar(jwt.getClaimAsString("picture"))
        .postCount(0L)
        .build();

    userRepository.save(user);
    var result = userMapper.toDto(user);
    return updateIsFirstSignInFlag(result, true);
  }

  private UserDto updateIsFirstSignInFlag(UserDto userDto, boolean flag) {
    userDto.setFirstSignIn(flag);
    return userDto;
  }
}
