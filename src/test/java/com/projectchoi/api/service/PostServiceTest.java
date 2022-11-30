package com.projectchoi.api.service;

import com.projectchoi.api.domain.Post;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreateDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void write_post_test() {
        // given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .title("글제목")
                .content("글내용")
                .build();

        // when
        postService.write(postCreateDto);

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("글제목", post.getTitle());
        assertEquals("글내용", post.getContent());
    }

    @Test
    @DisplayName("글 단건조회")
    void get_single_post_test() {
        // given
        Post requestPost = Post.builder()
                .title("title")
                .content("content")
                .build();
        postRepository.save(requestPost);

        // when
        Post getPost = postService.getSinglePost(requestPost.getId());

        // then
        assertNotNull(getPost);
        assertThat(getPost.getId()).isEqualTo(requestPost.getId());
        assertEquals("title", getPost.getTitle());
        assertEquals("content", getPost.getContent());
    }




}