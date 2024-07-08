package com.pard.namukkun.user.entity;

import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.postit.entity.PostIt;
import com.pard.namukkun.user.dto.UserCreateDTO;
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
    private String nickName;
    private String email;


    private Integer local = 0;

    private String profileImage; // kakao profile image

    @OneToOne
    private Post tempPost;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostIt> postIts;

    // 좋아요 누른 포스트
    @ElementCollection
    @CollectionTable(name = "user_up_post_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "up_comment_id")
    private List<Long> upPostList;

    // 좋아요 누른 덧글
    @ElementCollection
    @CollectionTable(name = "user_up_comment_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "up_comment_id")
    private List<Long> upCommentList;

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
    // setters
    public void setTempPost(Post tempPost) {
        this.tempPost = tempPost;
    }

    public void updateUserinfo(String nickName, Integer local, String image) {
        this.nickName = nickName;
        this.local = local;
        this.profileImage = image;
    }

    public void updateUpPostList(List<Long> list) {
        this.upPostList = list;
    }

    public void updateUpCommentList(List<Long> list) {
        this.upCommentList = list;
    }
}