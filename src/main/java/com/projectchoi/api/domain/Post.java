package com.projectchoi.api.domain;

import com.projectchoi.api.request.PostEdit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String authorId;

    @Lob // 자바에서는 String, DB 에서는 Long text 로 넘어가도록
    @NotBlank
    private String content;

    @Builder
    public Post(String title, String content, String authorId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    public void edit(PostEdit postEdit) {
        title =  postEdit.getTitle() != null ? postEdit.getTitle() : title;
        content = postEdit.getContent() != null ? postEdit.getContent() : content;
    }

}
