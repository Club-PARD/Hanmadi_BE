package com.pard.namukkun.post.controller;

import com.pard.namukkun.post.dto.PostCreateDTO;
import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;

    @PostMapping("")
    public String createPost(@RequestBody PostCreateDTO postCreateDTO) {
        postService.createPost(postCreateDTO);
        return "Post created";
    }

    @GetMapping("")
    public List<PostReadDTO> findAllPost() {
        return postService.readPost();
    }
}
