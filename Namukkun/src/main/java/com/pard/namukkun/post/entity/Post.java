package com.pard.namukkun.post.entity;

import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    private String content;
    private String region;

    // 첨부파일 넣는것도 추가해야함

    public Post(String title, String content, String region) {
        this.title = title;
        this.content = content;
        this.region = region;
    }

    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne
    private User user;

    public static Post toEntity(PostCreateDTO postCreateDTO) {
        return Post.builder()
                .title(postCreateDTO.getTitle())
                .content(postCreateDTO.getContent())
                .region(postCreateDTO.getRegion())
                .build();
    }
}
