package com.pard.namukkun.img.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor

public class Img {
    @Id
    private Long imageId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    public Img(Long imageId, String imgUrl) {
        this.imageId = imageId;
        this.imgUrl = imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
}
