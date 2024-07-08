package com.pard.namukkun.postit.dto;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.pard.namukkun.user.dto.UserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostItCreateInfoDTO {
    private Long postItId;
    private UserInfoDTO userInfo;
}
