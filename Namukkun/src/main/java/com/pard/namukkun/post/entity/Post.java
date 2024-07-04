package com.pard.namukkun.post.entity;

import com.pard.namukkun.Data;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.image.entity.Image;
import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title; // 제목
    private Integer postRegion; // 지역
    private Integer upCountPost; // 추천수
    private String proBackground; // 제안배경
    private String solution; // 해결방
    private String benefit; // 기대효과
    private boolean isDone; // 작성 후 7일이 지난거
    private boolean isReturn; // 게시물 업로드 확인
    private String postTime; // 작성된 날짜 및 시간

    @JoinColumn(nullable = false, name = "user_Id")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3Attachment> s3Attachments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addS3Attachment(String fileUrl) {

        if(this.s3Attachments == null) {
            this.s3Attachments = new ArrayList<>();
        }
        S3Attachment s3Attachment = new S3Attachment();
        s3Attachment.setFileUrl(fileUrl);
        s3Attachment.setPost(this);
        this.s3Attachments.add(s3Attachment);
    }

    public static Post toEntity(PostCreateDTO postCreateDTO, User user) {
        Post post = Post.builder()
                .title(postCreateDTO.getTitle())
                .postRegion(postCreateDTO.getPostRegion())
                .upCountPost(postCreateDTO.getUpCountPost())
                .proBackground(postCreateDTO.getProBackground())
                .solution(postCreateDTO.getSolution())
                .benefit(postCreateDTO.getBenefit())
                .postTime(Data.getNowDate())
                .isDone(false)
                .user(user)
                .build();
        return post;
    }

    public void setIsReturn(boolean isReturn) {
        this.isReturn = isReturn;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updatePostRegion(Integer postRegion) {
        this.postRegion = postRegion;
    }

    public void updateUpCountPost(Integer upCountPost) {
        this.upCountPost = upCountPost;
    }

    public void updateProBackground(String proBackground) {
        this.proBackground = proBackground;
    }

    public void updateSolution(String solution) {
        this.solution = solution;
    }

    public void updateBenefit(String benefit) {
        this.benefit = benefit;
    }

}
