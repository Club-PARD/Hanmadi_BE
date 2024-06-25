package com.pard.namukkun.post.dto;

import com.pard.namukkun.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostCreateDTO {
    private String title;
    private String content;
    private String region;
    private User user;
    private Integer upCount;
}
