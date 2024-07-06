package com.pard.namukkun.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 간단한 정보만 보내주는 DTO
public class UserInfoDTO {
    private String nickName;
    private Integer local;
    private String profileImage;
}
