package com.pard.namukkun.user.controller;

import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserDeleteDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
import com.pard.namukkun.user.dto.UserUpdateDTO;
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
    @Operation(summary = "유저등록", description = "이름,이메일,authId를 통해 유저를 생성합니다.")
    public String createUser(@RequestBody UserCreateDTO dto) {
        userService.createUser(dto);
        return "User created";
    }

    @PatchMapping("/update/nickname")
    @Operation(summary = "유저 닉네임 변경", description = "유저의 아이디값,변경할 닉네임을 통해 유저를 수정합니다.")
    public String updateUser(@RequestBody UserUpdateDTO dto) {
        try {
            userService.updateUserNickname(dto);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestBody UserDeleteDTO dto) {
        userService.deleteUser(dto);
        return "success";
    }
//
//    @GetMapping("/mypage")
//    public UserReadDTO mypage(){
//
//    }


    @GetMapping("")
    public List<UserReadDTO> findAllUser() {
        return userService.findAllUser();
    }
}
