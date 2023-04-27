package com.projectchoi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectchoi.api.crypt.PasswordEncoder;
import com.projectchoi.api.domain.Users;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUp_test() throws Exception {
        // given
        SignUp signUp = SignUp.builder()
                .email("lucaschoi@gmail.com")
                .password("1234")
                .name("choi")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    public void signUp_fail_test() throws Exception {
        // given
        Users user = userRepository.save(Users.builder()
                .email("lucaschoi@gmail.com")
                .name("lee")
                .password(passwordEncoder.encrypt("asdf"))
                .build());

        SignUp signUp = SignUp.builder()
                .email("lucaschoi@gmail.com")
                .password("1234")
                .name("choi")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("이미 등록된 이메일입니다.", result.getResolvedException().getMessage()))
                .andDo(print());
    }


}
