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
    private List<String> imgUrls;

    public Img(Long imageId, List<String> imgUrl) {
        this.imageId = imageId;
        this.imgUrls = imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrls.add(imgUrl);
    }
}
