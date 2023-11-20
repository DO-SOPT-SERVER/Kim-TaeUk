package com.server.dosopt.seminar.dto.post;

import com.server.dosopt.seminar.domain.Post;

public record PostGetResponse(
        Long postId,
        String title,
        String content
) {
    public static PostGetResponse of(Post post) {
        return new PostGetResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent()
        );
    }
}
