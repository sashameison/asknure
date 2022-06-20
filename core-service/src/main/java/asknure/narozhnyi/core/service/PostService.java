package asknure.narozhnyi.core.service;

import static asknure.narozhnyi.core.dto.PostDto.Fields.comments;

import java.util.List;
import java.util.Optional;

import asknure.narozhnyi.core.dto.CommentCreateDto;
import asknure.narozhnyi.core.dto.MessageDto;
import asknure.narozhnyi.core.dto.PostCreateDto;
import asknure.narozhnyi.core.dto.PostDto;
import asknure.narozhnyi.core.dto.PostDtoResponse;
import asknure.narozhnyi.core.dto.PostSearchParam;
import asknure.narozhnyi.core.dto.PostUpdateDto;
import asknure.narozhnyi.core.exceptions.BadRequest;
import asknure.narozhnyi.core.exceptions.NotFoundException;
import asknure.narozhnyi.core.mapper.CommentMapper;
import asknure.narozhnyi.core.mapper.PostMapper;
import asknure.narozhnyi.core.model.Comment;
import asknure.narozhnyi.core.model.Post;
import asknure.narozhnyi.core.repository.PostRepository;
import asknure.narozhnyi.core.util.AuthUtil;
import asknure.narozhnyi.core.util.ColorGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private static final String ID = "_id";
  private final PostRepository postRepository;
  private final UserService userService;
  private final S3Service s3Service;
  private final MongoTemplate mongoTemplate;
  private final PostMapper postMapper;
  private final CommentMapper commentMapper;
  private final MailSenderService mailSenderService;

  public Page<PostDtoResponse> findAll(Pageable pageable, String searchParam, List<String> categories) {
    return postRepository.findPostBy(pageable, buildParam(searchParam, categories))
        .map(postMapper::toResponseDto);
  }

  public PostDto findById(String postId) {
    return postRepository.findPostById(postId)
        .map(postMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public PostDto save(PostCreateDto postDto) {
    return Optional.of(postDto)
        .map(postMapper::toEntity)
        .map(this::updateColor)
        .map(this::updateUserPostsCount)
        .map(postRepository::save)
        .map(postMapper::toDto)
        .orElseThrow(BadRequest::new);
  }

  public PostDto update(String postId, PostUpdateDto postDto) {
    return postRepository.findPostById(postId)
        .map(post -> {
          Optional.ofNullable(postDto.getTitle())
              .ifPresent(post::setTitle);
          Optional.ofNullable(postDto.getText())
              .ifPresent(post::setText);
          return post;
        })
        .map(postRepository::save)
        .map(postMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  public Comment saveComment(String postId, CommentCreateDto commentDto) {
    return Optional.of(commentDto)
        .map(commentMapper::toEntity)
        .map(comment -> updateComment(postId, comment))
        .orElseThrow(BadRequest::new);
  }

  public String deleteById(String id) {
    return postRepository.findPostById(id)
        .map(this::deletePostById)
        .orElseThrow(NotFoundException::new);
  }

  public Page<PostDtoResponse> findByCreator(Pageable pageable) {
    var jwt = AuthUtil.getJwt();
    return postRepository.findPostByCreatedBy(pageable, jwt.getClaimAsString("name"))
        .map(postMapper::toResponseDto);
  }

  public void saveAttachment(String postId, MultipartFile multipartFile) {
    postRepository.findPostById(postId)
        .map(post -> uploadFile(post, multipartFile))
        .orElseThrow(NotFoundException::new);
  }

  private Post updateColor(Post post) {
    post.setColor(ColorGenerator.generateColor());
    return post;
  }

  private Post updateUserPostsCount(Post post) {
    userService.updateUserPostsCount();
    return post;
  }

  private Comment updateComment(String postId, Comment comment) {
    AuthUtil.setAuthor(comment);
    updatePostById(comment, postId);
    var postDto = findById(postId);
    var user = userService.findByName(postDto.getCreatedBy());
    notifyUserWithEmail(user.getEmail(), postDto.getTitle());
    return comment;
  }

  private String deletePostById(Post post) {
    postRepository.delete(post);
    return post.getId();
  }

  private Post uploadFile(Post post, MultipartFile multipartFile) {
    String photo = s3Service.uploadFile(multipartFile);
    post.getFiles().add(photo);
    return postRepository.save(post);
  }

  private void updatePostById(Comment comment, String postId) {
    var query = Query.query(Criteria.where(ID).is(postId));
    var push = new Update().push(comments, comment);

    mongoTemplate.updateFirst(query, push, Post.class);
  }

  private void notifyUserWithEmail(String creator, String title) {
    mailSenderService.sendSimpleMessage(buildMessageDto(creator, title));
  }

  private PostSearchParam buildParam(String searchParam, List<String> categories) {
    return PostSearchParam.builder()
        .searchParam(searchParam)
        .categories(categories)
        .build();
  }

  private MessageDto buildMessageDto(String email, String title) {
    return MessageDto.builder()
        .title(title)
        .receiver(email)
        .build();
  }
}