package com.pard.namukkun.image.service;

import com.pard.namukkun.image.dto.ImageCreateDTO;
import com.pard.namukkun.image.entity.Image;
import com.pard.namukkun.image.repo.ImageRepo;
import com.pard.namukkun.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepo imageRepo;

    public Image saveImage(ImageCreateDTO imageCreateDTO, Post post){
        byte[] imageData = Base64.getDecoder().decode(imageCreateDTO.getBase64Data());

        Image image = Image.toEntity(imageCreateDTO,imageData,post);
        return imageRepo.save(image);
    }
}
