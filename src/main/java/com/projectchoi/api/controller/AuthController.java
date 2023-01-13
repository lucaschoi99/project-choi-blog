package com.projectchoi.api.controller;


import com.projectchoi.api.request.Login;
import com.projectchoi.api.response.SessionResponse;
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

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        String accessToken = authService.signIn(login);
        return new SessionResponse(accessToken);
    }
}
