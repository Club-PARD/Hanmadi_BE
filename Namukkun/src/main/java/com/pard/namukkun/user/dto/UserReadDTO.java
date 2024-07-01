package com.pard.namukkun.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // null값은 빼고 가져옴

public class UserReadDTO {
    private Long userId;
    private Integer local;
    private String nickName;
    private String email;
    private String profileImage;

    public UserReadDTO(User user){
        this.userId = user.getUserId();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
        this.email = user.getEmail();
        this.local = user.getLocal();
    }

}
