package com.projectchoi.api.controller;

import com.projectchoi.api.request.PostCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String post(@RequestBody PostCreateDto params) {
        log.info("params= {}", params);
        return "Hello its my first blog project";
    }

}
