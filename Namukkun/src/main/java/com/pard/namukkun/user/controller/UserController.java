package com.pard.namukkun.user.controller;

import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    @Operation(summary = "유저등록",description = "이름,이메일,authId를 통해 유저를 생성합니다.")
    public String createUser(@RequestBody UserCreateDTO userCreateDTO){
        userService.createUser(userCreateDTO);
        return "User created";
    }

    @GetMapping("")
    public List<UserReadDTO> findAllUser(){
        return userService.findAllUser();
    }
}
