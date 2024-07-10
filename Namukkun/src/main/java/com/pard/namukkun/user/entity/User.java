package com.pard.namukkun.user.entity;

import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.img.entity.Img;
import com.pard.namukkun.post.entity.Post;
import com.pard.namukkun.postit.entity.PostIt;
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
    private String nickName;
    private String email;

    private String role; // 유저 권한 필드 추가


    private Integer local = 0;

    //-------------- 준현 수정 -----------
    @Lob
    @Column(columnDefinition = "TEXT")
    private String profileImage; // kakao profile image
    //---------------------------------

    @OneToOne
    private Post tempPost;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostIt> postIts = new ArrayList<>();

    // 좋아요 누른 포스트
    @ElementCollection
    @CollectionTable(name = "user_up_post_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "up_comment_id")
    private List<Long> upPostList = new ArrayList<>();

    // 좋아요 누른 덧글
    @ElementCollection
    @CollectionTable(name = "user_up_comment_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "up_comment_id")
    private List<Long> upCommentList = new ArrayList<>();

    // 이미지
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Img> imgs = new ArrayList<>();

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

    // ---------------- 준현 수정 -------------
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setImgs(List<Img> imgs) {
        this.imgs = imgs;
    }

    public void addImg(Img img) {
        img.setUser(this);
        this.imgs.add(img);
    }

    public void deleteImg(Img img) {
        this.imgs.remove(img);
    }
    // ---------------- 준현 수정 -------------
}