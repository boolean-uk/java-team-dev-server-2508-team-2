package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Post;
import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponse extends Response {
    public void set(List<Post> posts) {
        Data<List<Post>> data = new PostListData();
        data.set(posts);
        super.set(data);
    }
}
