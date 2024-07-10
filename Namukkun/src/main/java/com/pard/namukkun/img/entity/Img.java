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
    private Boolean publish;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    public static Img toEntity(User user, String imgUrl) {
        return Img.builder()
                .user(user)
                .imgUrl(imgUrl)
                .publish(false)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public void setImgUrl(String imgUrl, User user){
        this.imgUrl = imgUrl;
        this.user = user;
    }
}
