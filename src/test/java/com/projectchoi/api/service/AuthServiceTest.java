package com.projectchoi.api.service;

import com.projectchoi.api.crypt.PasswordEncoder;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.exception.DuplicateEmailException;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_test() {
        // given
        SignUp signUp = SignUp.builder()
                .email("lucaschoi@gmail.com")
                .password("1234")
                .name("choi")
                .build();

        // when
        authService.signUp(signUp);

        // then
        assertEquals(1, userRepository.count());

        Users user = userRepository.findAll().iterator().next();

        assertEquals(signUp.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(signUp.getPassword(), user.getPassword()));
        assertEquals(signUp.getName(), user.getName());
    }


    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signUp_fail_test() {
        // given
        Users user = userRepository.save(Users.builder()
                .email("lucaschoi@gmail.com")
                .name("lee")
                .password("5678")
                .build());

        SignUp signUp = SignUp.builder()
                .email("lucaschoi@gmail.com")
                .password("1234")
                .name("choi")
                .build();

        // expected
        assertThrows(DuplicateEmailException.class, () -> authService.signUp(signUp));

    }






}