package com.projectchoi.api.repository;

import com.projectchoi.api.domain.Post;
import com.projectchoi.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
