package com.pard.namukkun.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TempImgDTO {
    private String Img;

    public TempImgDTO(String img) {
        Img = img;
    }
}
