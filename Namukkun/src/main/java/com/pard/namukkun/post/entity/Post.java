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
    private Integer upCount;

    // 첨부파일 넣는것도 추가해야함
    @JoinColumn(nullable = false, name = "user_Id")
    @OneToOne
    private User user;

    public Post(String title, String content, String region, User user) {
        this.title = title;
        this.content = content;
        this.region = region;
        this.user = user;
        this.upCount = 0;
    }

    public static Post toEntity(PostCreateDTO postCreateDTO, User user, Integer upCount) {
        return Post.builder()
                .title(postCreateDTO.getTitle())
                .content(postCreateDTO.getContent())
                .region(postCreateDTO.getRegion())
                .user(user)
                .upCount(upCount)
                .build();
    }
}
