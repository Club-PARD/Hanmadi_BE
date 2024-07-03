package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PostReadDTO {
    private String title; // 제목
    private Integer postRegion; // 지역
    private Integer upCountPost; // 추천수
    private String proBackground; // 제안배경
    private String solution; //해결방법
    private String benefit; // 기대효과
    private String postTime; // 작성된 시간
    private List<String> fileName; // 첨부파일 url
    private boolean isDone; // 제출완료 (작성 후 7일이 지난거)
    private String userName;

    private List<S3AttachmentReadDTO> s3Attachments;

    public PostReadDTO(Post post) {
        this.title = post.getTitle();
        this.postRegion = post.getPostRegion();
        this.upCountPost = post.getUpCountPost();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
        this.userName = post.getUser().getNickName();
    }

    public PostReadDTO(Post post, List<S3AttachmentReadDTO> s3Attachments) {
        this.title = post.getTitle();
        this.postRegion = post.getPostRegion();
        this.upCountPost = post.getUpCountPost();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
        this.userName = post.getUser().getNickName();
        this.s3Attachments = s3Attachments;
    }

    public PostReadDTO(Post post, User user) {
        this.title = post.getTitle();
        this.postRegion = post.getPostRegion();
        this.upCountPost = post.getUpCountPost();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
    }

}
