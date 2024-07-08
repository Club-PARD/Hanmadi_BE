package com.pard.namukkun.post.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ImgDTO {
    private String Img;

    public ImgDTO(String img) {
        Img = img;
    }
}
