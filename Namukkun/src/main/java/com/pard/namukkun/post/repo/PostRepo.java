package com.pard.namukkun.post.repo;

import com.pard.namukkun.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
