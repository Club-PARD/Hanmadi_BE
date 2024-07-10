package com.pard.namukkun.img.repo;

import com.pard.namukkun.img.entity.Img;
import com.pard.namukkun.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImgRepo extends JpaRepository<Img, Long> {
}
