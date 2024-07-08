package com.pard.namukkun.img.dto;

import com.pard.namukkun.img.entity.Img;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ImgDTO {
    private Long usrId;
    private Img imgUrl;

    public ImgDTO(Long usrId, Img imgUrl) {
        this.usrId = usrId;
        this.imgUrl = imgUrl;
    }

    public void addImageUrl(Img imgUrl) {
        this.imgUrl = imgUrl;
    }
}
