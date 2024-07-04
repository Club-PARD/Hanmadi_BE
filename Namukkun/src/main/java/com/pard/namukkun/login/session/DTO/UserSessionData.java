package com.pard.namukkun.login.session.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSessionData {
    private Long userId;
    private String nickName;
    private Integer local;
    private String profileImage;

    public UserSessionData(UserSessionDTO dto) {
        this.userId = dto.getUserId();
        this.nickName = dto.getNickName();
        this.local = dto.getLocal();
        this.profileImage = dto.getProfileImage();
    }
}
