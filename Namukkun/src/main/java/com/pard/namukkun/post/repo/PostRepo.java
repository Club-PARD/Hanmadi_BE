package com.pard.namukkun.post.repo;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PostRepo extends JpaRepository<Post, Long> {
    List<Post> findByPostLocal(Integer postLocal);
}
