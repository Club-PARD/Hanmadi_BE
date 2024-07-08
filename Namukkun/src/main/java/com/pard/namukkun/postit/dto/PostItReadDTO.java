package com.pard.namukkun.postit.dto;

import com.pard.namukkun.postit.entity.PostIt;
import com.pard.namukkun.user.dto.UserInfoDTO;
import com.pard.namukkun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostItReadDTO {
    private Long postItId;
    // 유저
    private Long userId;

    private UserInfoDTO userInfoDTO;

    private Long commentId;

    // 디자인 옵션
    private Integer design;

    // 포스트잇 내용
    private String content;

    // 왼쪽 오른쪽
    private String section;

    // 위치
    private Float x;
    private Float y;
    private Float z;

    public PostItReadDTO(PostIt postIt) {
        User user = postIt.getUser();
        this.postItId = postIt.getId();
        try {
            System.out.println(postIt.getComment());
            this.commentId = postIt.getComment().getId();
        } catch (Exception Ignore) {}
        this.userId = postIt.getUser().getUserId();
        this.userInfoDTO = new UserInfoDTO(user.getNickName(), user.getLocal(), user.getProfileImage());
        this.design = postIt.getDesign();
        this.section = postIt.getSection();
        this.content = postIt.getContext();
        this.x = postIt.getX();
        this.y = postIt.getY();
        this.z = postIt.getZ();
    }
}
