package com.pard.namukkun.user.entity;

import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserUpdateDTO;
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
    private Long oauthID;
    private String kakaoId;
    private String nickName;
    private String email;

    private Integer local;

    private String profileImage; // kakao profile image

    //---------------------준현수정----------------//

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Post tempPost;

    public void setTempPost(Post tempPost) {
        this.tempPost = tempPost;
    }

    //-------------------------------------------//

//    private // TODO 유저가 쓴 글 저장
//
//    public User(User user) {
//        this.authId = user.getAuthId();
//        this.email = user.getEmail();
//        this.nickName = user.getNickName();
//    }

    public static User toEntity(UserCreateDTO userCreateDTO) {
        return User.builder()
                .oauthID(userCreateDTO.getOauthID())
                .nickName(userCreateDTO.getNickName())
                .profileImage(userCreateDTO.getProfileImage())
                .email(userCreateDTO.getEmail())
                .local(userCreateDTO.getLocal())
                .build();
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateLocal(Integer local) {
        this.local = local;
    }
}