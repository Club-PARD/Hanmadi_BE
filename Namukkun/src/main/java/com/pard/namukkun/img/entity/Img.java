package com.pard.namukkun.img.entity;

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

public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    public static Img toEntity(User user, String imgUrl) {
        return Img.builder()
                .user(user)
                .imgUrl(imgUrl)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
