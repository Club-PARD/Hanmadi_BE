package com.pard.namukkun.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PostUpdateDTO {
    private String title; // 제목
    private Integer postLocal; // 지역
    private Integer upCountPost; // 추천수
    private Integer postitCount; // 포스트잇 갯수
    private String proBackground; // 제안배경
    private String solution; //해결방법
    private String benefit; // 기대효과

    private List<String> fileName; // 첨부파일 url

    private boolean isReturn; // 게시물 업로드 확인

    private Long userId; // 작성자의 Id값 (고유 아이디)
}
