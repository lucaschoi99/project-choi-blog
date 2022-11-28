package com.projectchoi.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public String post(@RequestParam Map<String, String> params) {
        log.info("params= {}", params);
        return "Hello its my first blog project";
    }

}
