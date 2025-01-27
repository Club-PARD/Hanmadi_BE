package com.pard.namukkun.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateDTO {
    private Long oauthID;
    private String nickName;
    private String profileImage;
    private String email;
    private Integer local;

}
