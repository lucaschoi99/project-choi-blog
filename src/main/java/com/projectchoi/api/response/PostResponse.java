package com.projectchoi.api.response;


import com.projectchoi.api.domain.Post;
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
    private final String authorId;

    // 생성자 오버로딩
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthorId();
    }

    @Builder
    public PostResponse(Long id, String title, String content, String authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }
}
