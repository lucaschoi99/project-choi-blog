package com.projectchoi.api.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Builder
    public PostCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
