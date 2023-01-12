package com.projectchoi.api.controller;

import com.projectchoi.api.config.data.UserSession;
import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.request.PostEdit;
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

    @GetMapping("/foo")
    public Long foo(UserSession userSession) {
        log.info(">>>{}", userSession.id);
        return userSession.id;
    }

    @GetMapping("/bar")
    public String bar() {
        return "인증이 필요 없는 페이지입니다.";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
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

    // 게시글 수정
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        return postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
