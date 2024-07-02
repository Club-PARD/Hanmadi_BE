package com.pard.namukkun.post.service;

import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.repo.PostRepo;
import com.pard.namukkun.user.dto.UserReadDTO;
import com.pard.namukkun.user.entity.User;
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
        try{
            postRepo.save(Post.toEntity(postCreateDTO,postCreateDTO.getUser(),postCreateDTO.getUpCount()));
        } catch (Exception e){
            throw new RuntimeException("Error creating post",e);
        }
    }

//    public List<PostReadDTO> findAllPost(){
//        return postRepo.findAll()
//                .stream()
//                .map(post -> new PostReadDTO(post,
//                        new User(post.getUser())))
//                .collect(Collectors.toList());
//    }
}
