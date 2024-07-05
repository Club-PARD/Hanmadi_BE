package com.pard.namukkun.comment.entity;

import com.pard.namukkun.comment.dto.CommentReadDTO;
import com.pard.namukkun.postit.entity.PostIt;
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

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String content;


    //---------------------------------------------------
    private Long userId ;


    @JoinColumn(nullable = false, name = "postit-Id")
    @OneToOne
    private PostIt postIt;

    public static Comment toEntity(CommentReadDTO dto) {
        return Comment.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .build();
    }
    //---------------------------------------------------


}
