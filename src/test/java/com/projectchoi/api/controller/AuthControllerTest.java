package com.projectchoi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectchoi.api.domain.Session;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.repository.SessionRepository;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    public void login_test() throws Exception {
        // given
        userRepository.save(Users.builder()
                .name("ms-choi")
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build());

        Login login = Login.builder()
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 시 세션 1개 생성")
    public void login_session_test() throws Exception {
        // given
        Users usr = userRepository.save(Users.builder()
                .name("ms-choi")
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build());

        Login login = Login.builder()
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // 세션 생성 확인
        assertEquals(1L, usr.getSessions().size());

    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    public void login_session_response_test() throws Exception {
        // given
        Users usr = userRepository.save(Users.builder()
                .name("ms-choi")
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build());

        Login login = Login.builder()
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 인증이 필요한 페이지 접속 /foo")
    public void login_page_auth() throws Exception {
        // given
        Users user = Users.builder()
                .name("ms-choi")
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 인증이 필요한 페이지 접속")
    public void login_page_auth_with_not_validated_session() throws Exception {
        // given
        Users user = Users.builder()
                .name("ms-choi")
                .email("lucaschoi@gmail.com")
                .password("asdf")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-not-validated")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }


}
