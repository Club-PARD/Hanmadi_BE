package com.pard.namukkun.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateDTO {
    private Long id;
    private Integer Local;
    private String nickName;
}
