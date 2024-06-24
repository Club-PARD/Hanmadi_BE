package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PostReadDTO {
    private Long postId;
    private String title;
    private String content;
    private String region;

    public PostReadDTO(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.region = post.getRegion();
    }
}
