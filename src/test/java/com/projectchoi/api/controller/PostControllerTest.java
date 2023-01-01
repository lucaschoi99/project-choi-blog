package com.projectchoi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectchoi.api.domain.Post;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 정상 실행 확인")
    public void POST_normal_response_test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("글내용입니다 하하")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록 시 제목에 '바보'가 포함되어 있으면 예외처리 합니다.")
    public void POST_check_curse_test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("바보바보바보")
                .content("하하")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 title 필드 값은 필수 입니다.")
    public void POST_null_title_test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("글내용입니다 하하")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request!"))
                .andExpect(jsonPath("$.validation.title").value("must not be blank"))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청 시 DB에 값이 저장돼야 합니다.")
    public void POST_response_db_save_test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("글 내용입니다 하하")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("글 내용입니다 하하", post.getContent());
    }

    @Test
    @DisplayName("글 단건 조회")
    public void GET_single_post_test() throws Exception {
        // given
        Post post = Post.builder()
                .title("title")
                .content("content")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 단건 조회 존재X 게시글 - 실패 케이스")
    public void GET_single_post_fail_test() throws Exception {
        // expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @DisplayName("페이지 1 조회")
    public void GET_list_posts_test() throws Exception {
        // given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("choi's blog" + i)
                                .content("blog content" + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        // expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()", is(10)))
                        .andExpect(jsonPath("$[0].title").value("choi's blog30"))
                        .andExpect(jsonPath("$[0].content").value("blog content30"))
                        .andDo(print());

    }

    @Test
    @DisplayName("페이지 0을 요청하면 1페이지를 가져온다.")
    public void GET_page_0_performs_page_1_test() throws Exception {
        // given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("choi's blog" + i)
                                .content("blog content" + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        // expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()", is(10)))
                        .andExpect(jsonPath("$[0].title").value("choi's blog30"))
                        .andExpect(jsonPath("$[0].content").value("blog content30"))
                        .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정")
    public void edit_title_test() throws Exception {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("bin")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                        .andExpect(status().isOk())
                        .andDo(print());
    }

    @Test
    @DisplayName("글 내용 수정")
    public void edit_content_test() throws Exception {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("msChoi")
                .content("리버뷰용산")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                        .andExpect(status().isOk())
                        .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 게시글X - 실패 케이스")
    public void edit_post_fail_test() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("msChoi")
                .content("리버뷰용산")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @Test
    @DisplayName("글 삭제")
    public void delete_post_test() throws Exception {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


}