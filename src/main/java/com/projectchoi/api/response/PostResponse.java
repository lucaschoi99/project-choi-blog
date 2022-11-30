package com.projectchoi.api.response;


import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책에 맞는 클래스
 */

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        // 서비스 정책 요구 사항: title 항목은 최대 10글자까지 허용해 주세요.
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}