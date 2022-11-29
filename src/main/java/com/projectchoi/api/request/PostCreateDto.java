package com.projectchoi.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
public class PostCreateDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

}
