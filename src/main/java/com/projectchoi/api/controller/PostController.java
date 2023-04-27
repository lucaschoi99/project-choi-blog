package com.projectchoi.api.controller;

import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.request.PostSearch;
import com.projectchoi.api.response.PostResponse;
import com.projectchoi.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping("/posts")
    public Long post(@RequestBody @Valid PostCreate request) {
        request.validate();
        return postService.write(request);
    }

    // 게시글 단건 조회
    @GetMapping("/posts/{postId}")
    public PostResponse getSinglePost(@PathVariable Long postId) {
        return postService.getSinglePost(postId);
    }

    // 게시글 여러개 조회 - Pageable 버전
    @GetMapping("/posts-pageable")
    public List<PostResponse> getList_pageable(Pageable pageable) {
        return postService.getList_pageable(pageable);
    }

    // 게시글 여러개 조회 - querydsl 버전
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

//    // 게시글 수정
//    @PatchMapping("/posts/{postId}")
//    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request, @Auth UserSession userSession) {
//        return postService.edit(postId, request, userSession.id);
//    }
//
//    // 게시글 삭제
//    @DeleteMapping("/posts/{postId}")
//    public void delete(@PathVariable Long postId, @Auth UserSession userSession) {
//        postService.delete(postId, userSession.id);
//    }
}
