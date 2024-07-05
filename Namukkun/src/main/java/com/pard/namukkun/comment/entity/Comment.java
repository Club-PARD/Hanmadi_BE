package com.pard.namukkun.comment.entity;

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
    private Long userId;        // 작성자
    private Integer upCounter;  // 좋아요 수
    private String commentTime; // timetable
    private String content;     // 내용

    public static Comment toEntity(CommentCreateDTO dto) {
        return Comment.builder()
                .upCounter(0)
                .userId(dto.getUserId())
                .commentTime(Data.getNowDate())
                .content(dto.getContent())
                .build();
    }

    public Comment(CommentCreateDTO dto) {
        this.userId = dto.getUserId();
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


}
