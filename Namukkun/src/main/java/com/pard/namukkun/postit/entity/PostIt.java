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

    // 위치
    private Float x;
    private Float y;
    private Float z;


    @OneToOne(mappedBy = "postIt", orphanRemoval = false)
    private Comment comment;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Post post;        // 작성자

    public void updatePosition(Float x, Float y, Float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static PostIt toEntity(PostItCreateDTO dto, User user,Comment comment, String context) {
        return new PostIt().builder()
                .user(user)
                .comment(comment)
                .context(context)
                .x(dto.getX())
                .y(dto.getY())
                .z(dto.getZ()).
                build();
    }


//
//    public static PostIt toEntity(PostItMoveDTO dto, User user, String context ) {
//
//        return new PostIt().builder()
//                .user(user)
//                .commentId(())
//                .context(context)
////                .context(dto.getContext())
//                .x(dto.getX())
//                .y(dto.getY())
//                .z(dto.getZ())
//                .build();
//    }


    public void updateSection(String section) {
        this.section = section;
    }


}
