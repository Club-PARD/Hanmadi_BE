package com.pard.namukkun.comment.dto;

import com.pard.namukkun.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadDTO {
    private Long id;            // 덧글 아이디
    private Long userId;        // 작성자
    private Integer upCounter;  // 좋아요 수
    private String commentTime; // timetable
    private String content;     // 내용




    public CommentReadDTO(Comment comment){
        this.id = comment.getId();
        this.userId = comment.getUserId();
        this.upCounter = comment.getUpCounter();
        this.commentTime = comment.getCommentTime();
        this.content = comment.getContent();
    }
}
