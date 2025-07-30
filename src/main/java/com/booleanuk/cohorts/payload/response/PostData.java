package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Post;
import lombok.Getter;

@Getter
public class PostData extends Data<Post> {
    protected Post post;

    @Override
    public void set(Post post) {
        this.post = post;
    }
}
