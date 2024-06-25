package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.entity.User;
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
    private User user;
    private Integer upCount;

    public PostReadDTO(Post post, User user) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.region = post.getRegion();
        this.upCount = post.getUpCount();
        this.user = user;
    }
}
