package com.pard.namukkun.post.service;

import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PostService {
    private final PostRepo postRepo;

    // PostCreateDTO 받아서 postDTO 생성
    public void createPost(PostCreateDTO postCreateDTO) {
        postRepo.save(Post.toEntity(postCreateDTO));
    }

    public List<PostReadDTO> findAllPost(){
        return postRepo.findAll()
                .stream()
                .map(post -> new PostReadDTO(post))
                .collect(Collectors.toList());
    }
}
