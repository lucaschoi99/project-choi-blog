package com.projectchoi.api.controller;


import com.projectchoi.api.request.SignUp;
import com.projectchoi.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signUp")
    public void signUp(@RequestBody SignUp signUp) {
        authService.signUp(signUp);
    }
}
