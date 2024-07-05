package com.pard.namukkun.post.repo;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.dto.UserPostDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {

    public List<Post> findByIsDoneFalse();
    public List<Post> findByPostLocal(Integer postLocal);

}
