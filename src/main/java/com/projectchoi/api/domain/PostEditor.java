package com.projectchoi.api.domain;

import lombok.Builder;
import lombok.Getter;

/**
 * 수정할 수 있는 필드에 대해서 정의
 */

@Getter
public class PostEditor {

    private String title;
    private String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
