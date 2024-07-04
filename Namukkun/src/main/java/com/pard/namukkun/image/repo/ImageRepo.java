package com.pard.namukkun.image.repo;


import com.pard.namukkun.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Image, Long> {
}
