package com.projectchoi.api.service;

import com.projectchoi.api.domain.Session;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.exception.InvalidSignIn;
import com.projectchoi.api.repository.SessionRepository;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 - SessionRepository에 저장된 세션의 User는 로그인한 User와 같다.")
    void login_test() {
        // given
        Users user = userRepository.save(Users.builder()
                .name("ms-choi")
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build());

        Login login = Login.builder()
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build();

        // when
        String accessToken = authService.signIn(login);
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalAccessError::new);

        // then
        assertEquals(session.getUser().getId(), user.getId());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void login_fail_test() {
        // given
        Users user = userRepository.save(Users.builder()
                .name("Lee")
                .email("lee@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("lucaschoi@gmail.com")
                .password("1234")
                .build();

        // expected
        assertThrows(InvalidSignIn.class, () -> authService.signIn(login));
    }







}