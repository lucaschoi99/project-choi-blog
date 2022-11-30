package com.projectchoi.api.service;

import com.projectchoi.api.domain.Post;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreateDto;
import com.projectchoi.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
                .title("012345678912345")
                .content("content")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse getPost = postService.getSinglePost(requestPost.getId());

        // then
        assertNotNull(getPost);
        assertThat(getPost.getId()).isEqualTo(requestPost.getId());
        assertEquals("0123456789", getPost.getTitle());
        assertEquals("content", getPost.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void get_list_posts_test() {
        // given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("title1")
                        .content("content1")
                        .build(),
                Post.builder()
                        .title("title2")
                        .content("content2")
                        .build()
                )
        );

        // when
        List<PostResponse> posts = postService.getList();

        // then
        assertEquals(2L, posts.size());
    }


}