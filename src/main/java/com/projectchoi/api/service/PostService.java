package com.projectchoi.api.service;

import com.projectchoi.api.domain.Post;
import com.projectchoi.api.domain.PostEditor;
import com.projectchoi.api.repository.PostRepository;
import com.projectchoi.api.request.PostCreate;
import com.projectchoi.api.request.PostEdit;
import com.projectchoi.api.request.PostSearch;
import com.projectchoi.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostCreate postCreate) {
        // postCreateDto -> Entity 로 type cast 과정 필요
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        Post saved = postRepository.save(post);
        return saved.getId();
    }

    // 게시글 단건 조회
    public PostResponse getSinglePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 게시글 여러개 조회
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 글은 수정할 수 없습니다."));

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEdit();
        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
        return new PostResponse(post);
    }


}
