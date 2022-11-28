package com.projectchoi.api.request;

import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
public class PostCreateDto {

    public String title;
    public String content;
}
