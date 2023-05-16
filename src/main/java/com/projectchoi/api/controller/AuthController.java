package com.projectchoi.api.controller;


import com.projectchoi.api.request.SignUp;
import com.projectchoi.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지 입니다.";
    }

    @PostMapping("/auth/signUp")
    public void signUp(@RequestBody SignUp signUp) {
        authService.signUp(signUp);
    }
}
