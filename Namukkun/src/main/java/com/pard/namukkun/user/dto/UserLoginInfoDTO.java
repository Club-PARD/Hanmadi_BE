package com.pard.namukkun.user.dto;


// 유저가 로그인 하면 넘겨주는 DTO

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginInfoDTO {
    private String nickName;
    private Integer local;
    private String profileImage;
    private List<Long> postUpList;
    private List<Long> commentUpList;
}
