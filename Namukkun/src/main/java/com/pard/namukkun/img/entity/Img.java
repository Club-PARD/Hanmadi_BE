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
    private Long imageId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    public void setImgUrl(String imgUrl, User user){
        this.imgUrl = imgUrl;
        this.user = user;
    }
}
