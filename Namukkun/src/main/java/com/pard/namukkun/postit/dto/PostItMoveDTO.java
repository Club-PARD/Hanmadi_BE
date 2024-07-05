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
    private Long id;
    private Long userId;

    private Float x;
    private Float y;
    private Float z;
}
