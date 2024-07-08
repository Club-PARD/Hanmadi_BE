package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PostUpdateDTO {
    private String title; // 제목
    private int postLocal; // 지역
    private String proBackground; // 제안배경
    private String solution; //해결방법
    private String benefit; // 기대효과

    private List<String> fileNames; // 첨부파일 이름

    private Long userId; // 작성자의 Id값 (고유 아이디)

    public PostUpdateDTO(){

    }

    public PostUpdateDTO(Post post, List<String> fileNames) {
        this.title = post.getTitle();
        this.postLocal = post.getPostLocal();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.fileNames = fileNames;
        this.userId = post.getUser().getUserId();
    }
}
