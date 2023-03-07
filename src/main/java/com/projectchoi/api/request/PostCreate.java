package com.projectchoi.api.request;

import com.projectchoi.api.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String authorId;

    @Builder
    public PostCreate(String title, String content, String authorId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "글 제목에 '바보'가 포함될 수 없습니다.");
        }
    }
}
