package com.projectchoi.api.service;

import com.projectchoi.api.domain.Session;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.exception.DuplicateEmailException;
import com.projectchoi.api.exception.InvalidSignIn;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.Login;
import com.projectchoi.api.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

    public void signUp(SignUp signUp) {
        // 중복 이메일 체크
        Optional<Users> userEmail = userRepository.findByEmail(signUp.getEmail());
        if (userEmail.isPresent()) {
            throw new DuplicateEmailException();
        }

        Users user = Users.builder()
                .email(signUp.getEmail())
                .password(signUp.getPassword())
                .name(signUp.getName())
                .build();
        userRepository.save(user);
    }
}
