package asknure.narozhnyi.core.it;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import asknure.narozhnyi.core.mapper.PostMapper;
import asknure.narozhnyi.core.repository.PostRepository;
import asknure.narozhnyi.core.service.PostService;
import asknure.narozhnyi.core.util.StubUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IT
@RequiredArgsConstructor
@AutoConfigureMockMvc
public class PostControllerTest {

  private static final String POSTS_PATH = "/v1/posts";

  @Autowired
  private final MockMvc mockMvc;
  @Autowired
  private final PostRepository postRepository;
  @Autowired
  private final PostMapper postMapper;
  @Autowired
  private final PostService postService;

  private final ObjectMapper objectMapper;

  @MockBean
  private JwtDecoder jwtDecoder;

  @AfterEach
  void cleanUp() {
    postRepository.deleteAll();
  }

  @Test
  void shouldReturnAllPosts() throws Exception {
    postRepository.saveAll(List.of(
        StubUtils.stubPost("title1")
    ));

    mockMvc.perform(get(POSTS_PATH))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.content.size()", is(1)))
        .andExpect(jsonPath("$.content[0].title", is("title1")))
        .andExpect(status().is2xxSuccessful());
  }
  @Test
  @WithMockUser
  void shouldSavePost() throws Exception {
    var post = StubUtils.stubPost("test");
    mockMvc.perform(post(POSTS_PATH)
            .content(objectMapper.writeValueAsString(post))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
  }
}

