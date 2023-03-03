package com.projectchoi.api.controller;


import com.projectchoi.api.request.Login;
import com.projectchoi.api.request.SignUp;
import com.projectchoi.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity login(@RequestBody Login login) {
        String accessToken = authService.signIn(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/auth/signUp")
    public void signUp(@RequestBody SignUp signUp) {
        authService.signUp(signUp);
    }
}
