package com.pard.namukkun.postit.dto;

import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostItCreateDTO {

    // 유저
    private Long userId;

    // 연결된 덧글
    private Long commentId;

    // 포스트잇 내용
    private String context;

    // 위치
    private Float x;
    private Float y;
    private Float z;

}
