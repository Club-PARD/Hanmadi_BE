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
    private String title; // 제목
    private Integer postRegion; // 지역
    private Integer upCountPost; // 추천수
    private String problem; //문제정의
    private String solution; //해결방법
    private String benefit; // 기대효과
    private String postTime; // 작성된 시간
    private boolean isDone; // 제출완료 (작성 후 7일이 지난거)
    private String userName;

    public PostReadDTO(Post post) {
        this.title = post.getTitle();
        this.postRegion = post.getPostRegion();
        this.upCountPost = post.getUpCountPost();
        this.problem = post.getProblem();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
        this.userName = post.getUser().getNickName();
    }

    public PostReadDTO(Post post, User user) {
        this.title = post.getTitle();
        this.postRegion = post.getPostRegion();
        this.upCountPost = post.getUpCountPost();
        this.problem = post.getProblem();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
    }

}
