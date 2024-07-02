package com.pard.namukkun.login.session.DTO;

import com.pard.namukkun.user.dto.UserCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionUserDTO {
    private Long userId;
    private String nickName;
    private Integer local;
    private String profileImage;

    public SessionUserDTO(UserCreateDTO dto, Long id) {
        this.userId = id;
        this.nickName = dto.getNickName();
        this.profileImage = dto.getProfileImage();
        this.local = dto.getLocal();
    }
}
