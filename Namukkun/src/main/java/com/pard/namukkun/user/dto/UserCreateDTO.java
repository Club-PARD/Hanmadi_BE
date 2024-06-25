package com.pard.namukkun.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserCreateDTO {
    private String email;
    private String name;
    private Long authId;



    public UserCreateDTO(Long id, String email, String name){
        this.authId = id;
        this.email = email;
        this.name = name;
    }
}
