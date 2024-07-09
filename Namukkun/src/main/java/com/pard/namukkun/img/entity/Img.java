package com.pard.namukkun.img.entity;

import com.pard.namukkun.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Img {
    @Id
    private Long imageId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    public Img(Long imageId, String imgUrl, User user) {
        this.imageId = imageId;
        this.imgUrl = imgUrl;
        this.user = user;
    }

    public void setImgUrl(String imgUrl, User user){
        this.imgUrl = imgUrl;
        this.user = user;
    }
}
