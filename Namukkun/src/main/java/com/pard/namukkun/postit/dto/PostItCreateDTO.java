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

    private Long PostId;

    // 디자인 옵션
    private Integer design;

    // 왼쪽 오른쪽
    private String section;

    // 위치
    private Float x;
    private Float y;
    private Float z;

}
