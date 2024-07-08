package com.pard.namukkun.post.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UpCountInfoDTO {
    private int postUpCount; // 게시물 추천수
    private Long postId; // 추천을 누른 게시물 id
    private boolean state; // 추천을 누른 상태 true = 누름 / false = 안누름

    public UpCountInfoDTO(Long postId, boolean state, int postUpCount) {
        this.postId = postId;
        this.state = state;
        this.postUpCount = postUpCount;
    }
}
