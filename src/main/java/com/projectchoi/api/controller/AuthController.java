package com.projectchoi.api.controller;


import com.projectchoi.api.domain.Users;
import com.projectchoi.api.exception.InvalidSignIn;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public Users login(@RequestBody Login login) {
        // json email/password
        log.info(">>>login={}", login);

        // db 조회
        Users users = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSignIn::new);

        // 토큰 발급
        return users;
    }
}
