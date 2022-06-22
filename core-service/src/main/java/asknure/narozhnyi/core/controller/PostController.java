package asknure.narozhnyi.core.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.List;
import javax.validation.Valid;

import asknure.narozhnyi.core.dto.CommentCreateDto;
import asknure.narozhnyi.core.dto.PostCreateDto;
import asknure.narozhnyi.core.dto.PostDto;
import asknure.narozhnyi.core.dto.PostDtoResponse;
import asknure.narozhnyi.core.dto.PostUpdateDto;
import asknure.narozhnyi.core.model.Comment;
import asknure.narozhnyi.core.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

  private final PostService postService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Page<PostDtoResponse> getPosts(
      @RequestParam(name = "categories", required = false) List<String> categories,
      @RequestParam(name = "searchParam", required = false) String searchParam,
      @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
    return postService.findAll(pageable, searchParam, categories);
  }

  @GetMapping("/{post-id}")
  @ResponseStatus(HttpStatus.OK)
  public PostDto getById(@PathVariable("post-id") String postId) {
    return postService.findById(postId);
  }

  @GetMapping("/user")
  @ResponseStatus(HttpStatus.OK)
  public Page<PostDtoResponse> findUserPosts(
      @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
    return postService.findByUserId(pageable);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PostDto save(@RequestBody @Valid PostCreateDto postDto) {
    return postService.save(postDto);
  }

  @PutMapping("/{postId}")
  public PostDto update(@PathVariable("postId") String postId, @RequestBody PostUpdateDto postUpdateDto) {
    return postService.update(postId, postUpdateDto);
  }

  @PostMapping("/{post-id}")
  @ResponseStatus(HttpStatus.CREATED)
  public Comment saveComment(
      @PathVariable("post-id") String postId,
      @RequestBody @Valid CommentCreateDto comment) {
    return postService.saveComment(postId, comment);
  }

  @PostMapping("/{post-id}/upload")
  @ResponseStatus(HttpStatus.CREATED)
  public void saveAttachment(
      @PathVariable("post-id") String postId,
      @RequestParam("file") MultipartFile multipartFile) {
    postService.saveAttachment(postId, multipartFile);
  }

  @DeleteMapping("/{post-id}")
  @ResponseStatus(HttpStatus.OK)
  public String deleteById(@PathVariable("post-id") String postId) {
    return postService.deleteById(postId);
  }
}
