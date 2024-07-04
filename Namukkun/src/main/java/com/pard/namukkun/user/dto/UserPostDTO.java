package com.pard.namukkun.user.dto;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostDTO {
    private Long id;
    private PostReadDTO tempPost;
    private List<PostReadDTO> posts;


    public UserPostDTO(User user) {
        this.id = user.getUserId();
        this.tempPost = new PostReadDTO(user.getTempPost());
        this.posts = user.getPosts().stream().map(PostReadDTO::new).toList();
    }
}
