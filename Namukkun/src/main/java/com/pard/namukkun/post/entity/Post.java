package com.pard.namukkun.post.entity;

import com.pard.namukkun.Data;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.comment.entity.Comment;
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


    //--------------------------------------------------------
//    @JoinColumn(nullable = false, name = "comments_Id")
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<Comment> comments;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    //--------------------------------------------------------


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

    public void updatePost(String title, Integer postRegion, Integer upCountPost, String proBackground, String solution, String benefit) {
        this.title = title;
        this.postRegion = postRegion;
        this.upCountPost = upCountPost;
        this.proBackground = proBackground;
        this.solution = solution;
        this.benefit = benefit;
    }

}
