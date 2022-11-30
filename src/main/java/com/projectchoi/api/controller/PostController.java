package com.projectchoi.api.controller;

import com.projectchoi.api.request.PostCreateDto;
import com.projectchoi.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public Long post(@RequestBody @Valid PostCreateDto request) {
        return postService.write(request);
    }

}
