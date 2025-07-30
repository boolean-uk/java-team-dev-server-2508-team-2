package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.Post;
import lombok.Getter;

@Getter
public class PostResponse extends Response {

    public void set(Post post) {
        Data<Post> data = new PostData();
        data.set(post);
        super.set(data);
    }
}
