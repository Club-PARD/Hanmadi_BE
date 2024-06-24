package com.pard.namukkun.user.entity;

import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.dto.UserCreateDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;

    private String name;

//    private // 유저가 쓴 글

    public User(UserCreateDTO userCreateDTO) {
        this.email = userCreateDTO.getEmail();
        this.name = userCreateDTO.getName();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .name(userCreateDTO.getName())
                .email(userCreateDTO.getEmail())
                .build();
    }

}