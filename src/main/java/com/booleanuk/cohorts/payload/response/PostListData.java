package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListData extends Data<List<Post>> {
    protected List<Post> posts;

    @Override
    public void set(List<Post> postsList) {
        this.posts = postsList;
    }
}
