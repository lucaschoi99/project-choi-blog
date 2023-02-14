package com.projectchoi.api.service;

import com.projectchoi.api.domain.Post;
import com.projectchoi.api.exception.PostNotFound;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.request.PostEdit;
import com.projectchoi.api.request.PostSearch;
import com.projectchoi.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 작성
    @Transactional
    public Long write(PostCreate postCreate) {
        // postCreateDto -> Entity 로 type cast 과정 필요
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        Post saved = postRepository.save(post);
        return saved.getId();
    }

    // 게시글 단건 조회
    public PostResponse getSinglePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 게시글 여러개 조회 - querydsl 버전
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 게시글 여러개 조회 - Pageable 버전
    public List<PostResponse> getList_pageable(Pageable pageable) {
        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        post.edit(postEdit);
        return new PostResponse(post);
    }

    // 게시글 삭제
    @Transactional
    public void delete(Long id) {
        Post postToDelete = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(postToDelete);
    }
}
