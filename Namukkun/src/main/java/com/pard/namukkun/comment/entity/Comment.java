package com.pard.namukkun.comment.entity;

import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.postit.entity.PostIt;
import com.pard.namukkun.user.entity.User;
import jakarta.persistence.*;
import com.pard.namukkun.Data;
import com.pard.namukkun.comment.dto.CommentCreateDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 덧글 아이디
    private Integer upCounter;  // 좋아요 수
    private String commentTime; // timetable
    private String content;     // 내용
    private Boolean isTaken;    // 채택됨

    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;

    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;        // 작성자

    @OneToOne
    @JoinColumn(name = "postIt_id")
    private PostIt postIt;

    public static Comment toEntity(CommentCreateDTO dto, User user, Post post) {
        return Comment.builder()
                .upCounter(0)
                .user(user)
                .isTaken(false)
                .commentTime(Data.getNowDate())
                .content(dto.getContent())
                .post(post)
                .build();
    }

    public Comment(CommentCreateDTO dto, User user) {
        this.user = user;
        this.upCounter = 0;
        this.commentTime = Data.getNowDate();
        this.content = dto.getContent();
    }

    //--------------------------------------
    public void addUpCounter() {
        upCounter++;
    }

    public void minUpCounter() {
        upCounter--;
    }

    public void setIsTaken(Boolean isTaken) {
        this.isTaken = isTaken;
    }

}
