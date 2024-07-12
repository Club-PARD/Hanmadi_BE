package com.pard.namukkun.postit.entity;


import com.pard.namukkun.attachment.entity.S3Attachment;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.postit.dto.PostItCreateDTO;
import com.pard.namukkun.postit.dto.PostItMoveDTO;
import com.pard.namukkun.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostIt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성 유저
    @ManyToOne
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    // 포스트잇 내용
    private String context;

    // 포스트잇 섹션
    private String section;

    // 디자인
    private Integer design;

    // 위치
    private Float x;
    private Float y;
    private Float z;


    //    @OneToOne(mappedBy = "postIt", orphanRemoval = false)
//    @JoinColumn(name = "comment_id")
//    private Comment comment;
    @OneToOne(optional = true, orphanRemoval = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "post_Id", nullable = false)
    private Post post;

    public void updatePosition(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PostIt(PostItCreateDTO dto, User user, Comment comment, Post post, String context) {
        this.user = user;
        this.context = context;
        this.section = dto.getSection();
        this.design = dto.getDesign();
        this.comment = comment;
        this.post = post;
        this.x = dto.getX();
        this.y = dto.getY();
        this.z = dto.getZ();
    }

    public void updateSection(String section) {
        this.section = section;
    }

    public void setComment(Comment comment){
        this.comment = comment;
    }

}
