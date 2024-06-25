package com.pard.namukkun.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pard.namukkun.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null값은 빼고 가져옴

public class UserReadDTO {
    private Long userId;
    private String name;
    private String email;
    private Long authId;

    public UserReadDTO(User user){
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.authId = user.getAuthId();
    }

}
