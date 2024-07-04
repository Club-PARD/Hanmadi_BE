package com.pard.namukkun.image.entity;

import com.pard.namukkun.image.dto.ImageCreateDTO;
import com.pard.namukkun.post.entity.Post;
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

public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Lob
    private byte[] data;

    private String imageName;
    private String imageType;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public static Image toEntity(ImageCreateDTO imageCreateDTO, byte[] imageData, Post post){
        Image image = Image.builder()
                .imageName(imageCreateDTO.getImageName())
                .imageType(imageCreateDTO.getImageType())
                .data(imageData)
                .post(post)
                .build();
        return image;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
