package com.projectchoi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.projectchoi.api.domain.Post;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
    @DisplayName("/posts 요청 시 id를 반환해야 합니다.")
    public void POST_normal_response_test() throws Exception {
        // given
        PostCreateDto request = PostCreateDto.builder()
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
                .andExpect(content().string("1"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 title 필드 값은 필수 입니다.")
    public void POST_null_title_test() throws Exception {
        // given
        PostCreateDto request = PostCreateDto.builder()
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
        PostCreateDto request = PostCreateDto.builder()
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


}