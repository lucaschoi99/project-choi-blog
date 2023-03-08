package com.projectchoi.api.service;

import com.projectchoi.api.domain.Post;
import com.projectchoi.api.exception.PostNotFound;
import com.projectchoi.api.exception.UnauthorizedUser;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.request.PostEdit;
import com.projectchoi.api.request.PostSearch;
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
import static org.junit.jupiter.api.Assertions.*;
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
        PostCreate postCreateDto = PostCreate.builder()
                .title("글제목")
                .content("글내용")
                .authorId("1")
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
                .authorId("1")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse getPost = postService.getSinglePost(requestPost.getId());

        // then
        assertNotNull(getPost);
        assertThat(getPost.getId()).isEqualTo(requestPost.getId());
        assertEquals("012345678912345", getPost.getTitle());
        assertEquals("content", getPost.getContent());
    }

    @Test
    @DisplayName("글 단건조회 존재X - 실패 케이스")
    void get_single_post_fail_test() {
        // given
        Post requestPost = Post.builder()
                .title("012345678912345")
                .content("content")
                .authorId("1")
                .build();
        postRepository.save(requestPost);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.getSinglePost(requestPost.getId() + 1L);
        });

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
                                .authorId("1")
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        // when
        List<PostResponse> result = postService.getList(postSearch);

        // then
        assertEquals(10L, result.size());
        assertEquals("choi's blog30", result.get(0).getTitle());
    }

    @Test
    @DisplayName("Pageable 이용해 첫 페이지 글 조회")
    void get_first_page_pageable_test() {
        // given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i ->
                        Post.builder()
                                .title("choi's blog" + i)
                                .content("blog content" + i)
                                .authorId("1")
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        Pageable pageable = PageRequest.of(0, 10, DESC,"id");

        // when
        List<PostResponse> result = postService.getList_pageable(pageable);

        // then
        assertEquals(10L, result.size());
        assertEquals("choi's blog30", result.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정 성공 - 작성자 본인확인 검증 로직")
    void edit_AUTH_test() {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .authorId("1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("ms초이")
                .build();

        // when
        postService.edit(post.getId(), postEdit, Long.valueOf(post.getAuthorId()));

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글 제목 수정 과정에서 오류가 발생했습니다. 글 id=" + post.getId()));
        assertEquals("ms초이", changedPost.getTitle());
        assertEquals("반포자이", changedPost.getContent());
    }

    @Test
    @DisplayName("글 제목 수정 실패 - 작성자 본인확인 검증 로직")
    void edit_AUTH_fail_test() {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .authorId("1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .content("리버뷰용산")
                .build();

        // expected
        assertThrows(UnauthorizedUser.class, () ->
                postService.edit(post.getId(), postEdit, Long.valueOf(post.getAuthorId()) + 1L));

    }

    @Test
    @DisplayName("글 수정 존재X- 실패 케이스")
    void edit_NOT_EXISTS_fail_test() {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .authorId("1")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .content("리버뷰용산")
                .build();

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit, Long.valueOf(post.getAuthorId()));
        });
    }

    @Test
    @DisplayName("글 삭제 성공 - 작성자 본인확인 검증 로직")
    void delete_post_AUTH_test() {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .authorId("1")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId(), Long.valueOf(post.getAuthorId()));

        // then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 삭제 실패 - 작성자 본인확인 검증 로직")
    void delete_post_AUTH_fail_test() {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .authorId("1")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(UnauthorizedUser.class, () ->
                postService.delete(post.getId(), Long.valueOf(post.getAuthorId()) + 1L));
    }

    @Test
    @DisplayName("글 삭제 실패- 존재X")
    void delete_post_NON_EXISTS_fail_test() {
        // given
        Post post = Post.builder()
                .title("msChoi")
                .content("반포자이")
                .authorId("1")
                .build();
        postRepository.save(post);

        // expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L, Long.valueOf(post.getAuthorId()));
        });
    }


}