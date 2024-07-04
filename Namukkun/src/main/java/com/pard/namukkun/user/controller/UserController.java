package com.pard.namukkun.user.controller;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.user.dto.*;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

//    @PostMapping("")
//    @Operation(summary = "유저등록", description = "이름,이메일,authId를 통해 유저를 생성합니다.")
//    public String createUser(@RequestBody UserCreateDTO dto) {
//        userService.createUser(dto);
//        return "User created";
//    }

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

    @GetMapping("/posts")
    public UserPostDTO getUserPosts(@RequestParam("id") Long id) { // debug 용
        try {
            return userService.getUserPosts(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.info("유저 없음");
            return null;
        }
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestBody UserDeleteDTO dto) {
        userService.deleteUser(dto);
        return "success";
    }

    @GetMapping("")
    public List<UserReadDTO> findAllUser() {
        return userService.findAllUser();
    }
}
