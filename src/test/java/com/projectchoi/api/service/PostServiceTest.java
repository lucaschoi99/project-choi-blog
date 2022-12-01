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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
    @DisplayName("1페이지 글 조회")
    void get_list_posts_test() {
        // given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("choi's blog" + i)
                                .content("blog content" + i)
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        Pageable pageable = PageRequest.of(0, 5, DESC, "id");

        // when
        List<PostResponse> result = postService.getList(pageable);

        // then
        assertEquals(5L, result.size());
        assertEquals("choi's blog30", result.get(0).getTitle());
        assertEquals("blog content26", result.get(4).getContent());
    }


}