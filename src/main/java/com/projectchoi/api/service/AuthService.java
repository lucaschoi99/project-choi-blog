package com.projectchoi.api.service;

import com.projectchoi.api.domain.Session;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.exception.InvalidSignIn;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signIn(Login login) {
        // db 조회
        Users user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSignIn::new);

        Session session = user.addSession();
        return session.getAccessToken();
    }

}
