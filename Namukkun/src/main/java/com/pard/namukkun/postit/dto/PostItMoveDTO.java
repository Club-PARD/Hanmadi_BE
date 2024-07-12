package com.pard.namukkun.postit.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostItMoveDTO {
    private Long postItId;
    private Long postId;
    private Long commentId; // null 일 수 있음
//    private Long userId;


    private Float x;
    private Float y;
    private Float z;
}
