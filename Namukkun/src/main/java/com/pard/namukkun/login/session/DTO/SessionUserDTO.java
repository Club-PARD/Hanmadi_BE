package com.pard.namukkun.login.session.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionUserDTO {
    private Long userId;
    private Integer local;
    private String nickName;
    private String profileImage;
}
