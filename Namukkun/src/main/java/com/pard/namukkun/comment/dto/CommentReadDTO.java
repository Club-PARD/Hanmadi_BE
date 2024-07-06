package com.pard.namukkun.comment.dto;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.pard.namukkun.comment.entity.Comment;
import com.pard.namukkun.user.dto.UserInfoDTO;
import com.pard.namukkun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReadDTO {
    private Long id;                    // 덧글 아이디
    private UserInfoDTO userInfoDTO;    // 유저 정보
    private Long userId;                // 작성자
    private Integer upCounter;          // 좋아요 수
    private String commentTime;         // timetable
    private String content;             // 내용
    private Boolean isTaken;            // 채택됨

    public CommentReadDTO(Comment comment) {
        User user = comment.getUser();
        this.id = comment.getId();
        this.userInfoDTO = new UserInfoDTO(user.getNickName(), user.getLocal(), user.getProfileImage());
        this.userId = comment.getUser().getUserId();
        this.upCounter = comment.getUpCounter();
        this.commentTime = comment.getCommentTime();
        this.content = comment.getContent();
        this.isTaken = comment.getIsTaken();
    }
}
