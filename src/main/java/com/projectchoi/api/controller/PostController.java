package com.projectchoi.api.controller;

import com.projectchoi.api.request.PostCreateDto;
import com.projectchoi.api.request.PostSearch;
import com.projectchoi.api.response.PostResponse;
import com.projectchoi.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Long post(@RequestBody @Valid PostCreateDto request) {
        return postService.write(request);
    }

    // 게시글 단건 조회
    @GetMapping("/posts/{postId}")
    public PostResponse getSinglePost(@PathVariable Long postId) {
        return postService.getSinglePost(postId);
    }

    // 게시글 여러개 조회
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }
}
