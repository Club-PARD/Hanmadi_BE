package com.pard.namukkun.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreateDTO {
    private Integer local;
    private String nickName;
    private String email;
    private String profileImage;
}
