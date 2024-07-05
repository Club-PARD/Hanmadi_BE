package com.pard.namukkun.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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


    @ElementCollection
    @CollectionTable(name = "user_up_comment_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "up_comment_id")
    private List<Long> upList;


    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .oauthID(userCreateDTO.getOauthID())
                .nickName(userCreateDTO.getNickName())
                .profileImage(userCreateDTO.getProfileImage())
                .email(userCreateDTO.getEmail())
                .local(userCreateDTO.getLocal())
                .build();
    }


    //----------------------------------------------

    public void setTempPost(Post tempPost) {
        this.tempPost = tempPost;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateLocal(Integer local) {
        this.local = local;
    }
    public void updateUpList(List<Long> list) {
        this.upList = list;
    }
}