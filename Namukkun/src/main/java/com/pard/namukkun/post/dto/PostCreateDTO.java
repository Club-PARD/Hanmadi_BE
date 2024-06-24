package com.pard.namukkun.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostCreateDTO {
    private String title;
    private String content;
    private String region;
}
