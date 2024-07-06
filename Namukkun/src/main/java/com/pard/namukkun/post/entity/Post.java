package com.pard.namukkun.post.entity;

import com.pard.namukkun.Data;
import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.postit.entity.PostIt;
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
    private Integer postLocal; // 지역
    private Integer upCountPost; // 추천수
    private Integer postitCount; // 포스트잇 갯수
    private String proBackground; // 제안배경
    private String solution; // 해결방안
    private String benefit; // 기대효과
    private String deadLine; // 마감기한까지 남은 날짜
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
    // 댓글
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    // 포스트잇
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostIt> postIts;
    //--------------------------------------------------------


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3Attachment> s3Attachments = new ArrayList<>();

    @Lob
    private List<String> fileUrls = new ArrayList<>();

    public void addS3Attachment(String fileUrl) {

        if (this.s3Attachments == null) {
            this.s3Attachments = new ArrayList<>();
        }
        S3Attachment s3Attachment = new S3Attachment();
        s3Attachment.setFileUrl(fileUrl,this);
        this.s3Attachments.add(s3Attachment);
    }

    public static Post toEntity(PostCreateDTO postCreateDTO,String proBackgroundText,
                                String solutionText, String benefitText, User user) {
        Post post = Post.builder()
                .title(postCreateDTO.getTitle())
                .postLocal(postCreateDTO.getPostLocal())
                .proBackground(proBackgroundText)
                .solution(solutionText)
                .benefit(benefitText)
                .postTime(Data.getNowDate())
                .isDone(false)
                .user(user)
                .build();
        return post;
    }

    public void setInitial(boolean isReturn, Long deadLine) {
        this.isReturn = isReturn;
        this.deadLine = deadLine.toString();
    }
    //----------------------------------
    public void setIsDone(boolean isDone) { this.isDone = isDone; }
    public void setPostitCount(Integer postitCount) { this.postitCount = postitCount; }
    //----------------------------------

    public void updatePost(String title, Integer postLocal, Integer upCountPost,Integer postitCount, String proBackground, String solution, String benefit) {
        this.title = title;
        this.postLocal = postLocal;
        this.upCountPost = upCountPost;
        this.postitCount = postitCount;
        this.proBackground = proBackground;
        this.solution = solution;
        this.benefit = benefit;
    }

    public void increaseUpCountPost() {
        this.upCountPost++;
    }

    public void decreaseUpCountPost() {
        this.upCountPost--;
    }

}
