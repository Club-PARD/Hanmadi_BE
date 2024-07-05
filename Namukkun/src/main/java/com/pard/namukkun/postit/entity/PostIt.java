package com.pard.namukkun.postit.entity;


import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.postit.dto.PostItCreateDTO;
import com.pard.namukkun.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


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

    // 연결된 덧글
//    @JoinColumn(nullable = false, name = "commentId")
//    @OneToOne
//    private Comment comment;
    private Long commentId;


    // 포스트잇 내용
    private String context;

    // 위치
    private Float x;
    private Float y;
    private Float z;

    public static PostIt toEntity(PostItCreateDTO dto, User user) {

        return new PostIt().builder()
                .user(user)
                .commentId(dto.getCommentId())
                .context(dto.getContext())
                .x(dto.getX())
                .y(dto.getY())
                .z(dto.getZ()).
                build();
    }
}
