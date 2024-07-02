package com.pard.namukkun.post.service;

import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PostService {

    @Autowired
    private final PostRepo postRepo;

    @Autowired
    private final UserRepo userRepo;

    // PostCreateDTO 받아서 postDTO 생성
    public void createPost(PostCreateDTO postCreateDTO) {

        // post 정보 저장할 때 User의 모든 정보 받을 필요 없이 User Id만 받고
        // UserId로 User 찾아서 저장한 뒤에 Post 생성후 save함.
        User user = userRepo.findById(postCreateDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("Error creating post -> "+postCreateDTO.getUserId()));

        Post post = Post.toEntity(postCreateDTO, user);
        postRepo.save(post);
    }

    public List<PostReadDTO> readPost() {
        return postRepo.findAll()
                .stream()
                .map(post -> new PostReadDTO(post))
                .collect(Collectors.toList());
    }

}
