package com.pard.namukkun.user.entity;

import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.dto.UserCreateDTO;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

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
    private Long authId;
    private String name;

//    private // 유저가 쓴 글

    public User(User user) {
        this.authId = user.getAuthId();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .name(userCreateDTO.getName())
                .email(userCreateDTO.getEmail())
                .authId(userCreateDTO.getAuthId())
                .build();
    }

}