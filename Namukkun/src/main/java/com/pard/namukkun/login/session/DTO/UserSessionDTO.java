package com.pard.namukkun.login.session.DTO;

import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSessionDTO {
    private Long userId;
    private String nickName;
    private Integer local;
    private String profileImage;


    public UserSessionDTO(UserSessionData data) {
        this.userId = data.getUserId();
        this.nickName = data.getNickName();
        this.profileImage = data.getProfileImage();
        this.local = data.getLocal();
    }

    public UserSessionDTO(UserCreateDTO dto, Long id) {
        this.userId = id;
        this.nickName = dto.getNickName();
        this.profileImage = dto.getProfileImage();
        this.local = dto.getLocal();
    }

    public UserSessionDTO(User user) {
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
        this.local = user.getLocal();
    }
}
