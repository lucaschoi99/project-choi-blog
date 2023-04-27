package com.projectchoi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectchoi.api.crypt.PasswordEncoder;
import com.projectchoi.api.domain.Post;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.repository.UserRepository;
import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.project-choi.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 등록")
    void post_create() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("새로운 제목")
                .content("새로운 내용")
                .authorId("1")
                .build();

        // Object -> String
        String json = objectMapper.writeValueAsString(request);

        // expected
        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("글 제목")
                                        .attributes(key("required").value("Y")),
                                fieldWithPath("content").description("글 내용")
                                        .attributes(key("required").value("Y")),
                                fieldWithPath("authorId").description("작성자")
                                        .attributes(key("required").value("Y"))
                        )
                ));
    }

    @Test
    @DisplayName("글 단건 조회")
    void post_inquiry() throws Exception {
        // given
        Post post = Post.builder()
                .title("새로운 제목")
                .content("새로운 내용")
                .authorId("1")
                .build();
        postRepository.save(post);

        // expected
        this.mockMvc.perform(get("/posts/{postId}", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                                parameterWithName("postId").description("조회할 글의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("글 ID"),
                                fieldWithPath("title").description("글 제목"),
                                fieldWithPath("content").description("글 내용"),
                                fieldWithPath("authorId").description("작성자")
                        )
                ));
    }

    @Test
    @DisplayName("글 여러개 조회")
    void post_getList_inquiry() throws Exception {
        // given
        List<Post> posts = IntStream.rangeClosed(1, 30)
                .mapToObj(i ->
                        Post.builder()
                                .title("새로운 제목" + i)
                                .content("새로운 내용" + i)
                                .authorId("1")
                                .build())
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        // expected
        this.mockMvc.perform(get("/posts?page=1&size=10")
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-list-inquiry",
                        pathParameters(
                                parameterWithName("page").description("조회할 페이지 번호"),
                                parameterWithName("size").description("페이지당 글 개수")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("글 ID"),
                                fieldWithPath("[].title").description("글 제목"),
                                fieldWithPath("[].content").description("글 내용"),
                                fieldWithPath("[].authorId").description("작성자")
                        )
                ));
    }

//
//    @Test
//    @DisplayName("글 수정")
//    void post_edit() throws Exception {
//        // given
//        // 유저 등록후 로그인
//        Users user = userRepository.save(Users.builder()
//                .name("minsoo choi")
//                .email("lucaschoi@gmail.com")
//                .password(passwordEncoder.encrypt("asdf"))
//                .build());
//
//        Login login = Login.builder()
//                .email("lucaschoi@gmail.com")
//                .password("asdf")
//                .build();
//
//        // 유저 세션 값 -> Cookie 전달
//        String accessToken = authService.signIn(login);
//        Cookie cookie = new Cookie("SESSION", accessToken);
//
//        // 글 등록 후 수정 요청
//        Post post = Post.builder()
//                .title("새로운 제목")
//                .content("새로운 내용")
//                .authorId(String.valueOf(user.getId()))
//                .build();
//        postRepository.save(post);
//
//        PostEdit postEdit = PostEdit.builder()
//                .title("수정 제목")
//                .content("수정 내용")
//                .build();
//
//        // expected
//        this.mockMvc.perform(patch("/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                        .accept(APPLICATION_JSON)
//                        .cookie(cookie)
//                        .content(objectMapper.writeValueAsString(postEdit))
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-edit",
//                        pathParameters(
//                                parameterWithName("postId").description("수정할 글의 ID")
//                        ),
//                        requestFields(
//                                fieldWithPath("title").description("수정 제목")
//                                        .attributes(key("required").value("Y")),
//                                fieldWithPath("content").description("수정 내용")
//                                        .attributes(key("required").value("Y"))
//                        ),
//                        responseFields(
//                                fieldWithPath("id").description("수정된 글ID"),
//                                fieldWithPath("title").description("수정된 제목"),
//                                fieldWithPath("content").description("수정된 내용"),
//                                fieldWithPath("authorId").description("수정된 글작성자")
//                        )
//                ));
//    }
//
//    @Test
//    @DisplayName("글 삭제")
//    void post_delete() throws Exception {
//        // given
//        // 유저 등록후 로그인
//        Users user = userRepository.save(Users.builder()
//                .name("minsoo choi")
//                .email("lucaschoi@gmail.com")
//                .password(passwordEncoder.encrypt("asdf"))
//                .build());
//
//        Login login = Login.builder()
//                .email("lucaschoi@gmail.com")
//                .password("asdf")
//                .build();
//
//        // 유저 세션 값 -> Cookie 전달
//        String accessToken = authService.signIn(login);
//        Cookie cookie = new Cookie("SESSION", accessToken);
//
//        // 글 등록 후 삭제 요청
//        Post post = Post.builder()
//                .title("새로운 제목")
//                .content("새로운 내용")
//                .authorId(String.valueOf(user.getId()))
//                .build();
//        postRepository.save(post);
//
//        // expected
//        this.mockMvc.perform(delete("/posts/{postId}", post.getId())
//                        .contentType(APPLICATION_JSON)
//                        .accept(APPLICATION_JSON)
//                        .cookie(cookie)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("post-delete",
//                        pathParameters(
//                                parameterWithName("postId").description("삭제할 글의 ID")
//                        )
//                ));
//    }

}
