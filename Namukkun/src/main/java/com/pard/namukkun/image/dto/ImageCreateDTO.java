package com.pard.namukkun.image.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ImageCreateDTO {
    private String imageName;
    private String imageType;
    private String base64Data;
}
