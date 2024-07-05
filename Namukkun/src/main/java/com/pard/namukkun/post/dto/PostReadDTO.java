package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.attachment.dto.S3AttachmentReadDTO;
import com.pard.namukkun.image.dto.ImageCreateDTO;
import com.pard.namukkun.image.dto.ImageReadDTO;
import com.pard.namukkun.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PostReadDTO {
    private Long postId;
    private String title; // 제목
    private Integer postLocal; // 지역
    private Integer upCountPost; // 추천수
    private Integer postitCount; // 포스트잇 갯수
    private String proBackground; // 제안배경
    private String solution; //해결방법
    private String benefit; // 기대효과
    private String postTime; // 작성된 시간

    private List<String> fileName; // 첨부파일 url

    private boolean isDone; // 작성 후 7일이 지난거
    private boolean isReturn; // 게시물 업로드 확인
    private String userName;

    private List<S3AttachmentReadDTO> s3Attachments;
    private List<ImageReadDTO> images; // Base64로 인코딩된 image들

    public PostReadDTO(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.postLocal = post.getPostLocal();
        this.upCountPost = post.getUpCountPost();
        this.postitCount = post.getPostitCount();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
        this.userName = post.getUser().getNickName();
    }

    public PostReadDTO(Post post, List<S3AttachmentReadDTO> s3Attachments) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.postLocal = post.getPostLocal();
        this.upCountPost = post.getUpCountPost();
        this.postitCount = post.getPostitCount();
        this.proBackground = post.getProBackground();
        this.solution = post.getSolution();
        this.benefit = post.getBenefit();
        this.isDone = post.isDone();
        this.postTime = post.getPostTime();
        this.userName = post.getUser().getNickName();
        this.s3Attachments = s3Attachments;

        this.images = post.getImages().stream()
                .map(ImageReadDTO::new)
                .collect(Collectors.toList());
    }


}
