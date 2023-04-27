package com.projectchoi.api.service;

import com.projectchoi.api.crypt.PasswordEncoder;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.exception.DuplicateEmailException;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUp signUp) {
        // 중복 이메일 체크
        Optional<Users> userEmail = userRepository.findByEmail(signUp.getEmail());
        if (userEmail.isPresent()) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encrypt(signUp.getPassword());

        Users user = Users.builder()
                .email(signUp.getEmail())
                .password(encodedPassword)
                .name(signUp.getName())
                .build();
        userRepository.save(user);
    }
}
