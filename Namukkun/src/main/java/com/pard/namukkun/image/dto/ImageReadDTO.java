package com.pard.namukkun.image.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.image.entity.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ImageReadDTO {
    private String imageName;
    private String imageType;
    private String base64Data;

    public ImageReadDTO(Image image) {
        this.imageName = image.getImageName();
        this.imageType = image.getImageType();
        this.base64Data = Base64.getEncoder().encodeToString(image.getData());
    }
}
