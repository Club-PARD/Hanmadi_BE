package com.pard.namukkun.user.entity;

import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
import jakarta.persistence.*;
import lombok.*;

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
    private Long oauthID;
    private String kakaoId;
    private String nickName;
    private String email;

    private Integer local;

    private String profileImage; // kakao profile image

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToOne
    private Post tempPost;

    public void setTempPost(Post tempPost) {
        this.tempPost = tempPost;
    }

    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .oauthID(userCreateDTO.getOauthID())
                .nickName(userCreateDTO.getNickName())
                .profileImage(userCreateDTO.getProfileImage())
                .email(userCreateDTO.getEmail())
                .local(userCreateDTO.getLocal())
                .build();
    }
    public static User toEntity(UserReadDTO dto) {
        return User.builder()
                .userId(dto.getUserId())
                .nickName(dto.getNickName())
                .profileImage(dto.getProfileImage())
                .email(dto.getEmail())
                .local(dto.getLocal())
                .build();
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateLocal(Integer local) {
        this.local = local;
    }
}