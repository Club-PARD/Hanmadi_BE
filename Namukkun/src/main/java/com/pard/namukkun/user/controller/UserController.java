package com.pard.namukkun.user.controller;

import com.pard.namukkun.post.dto.PostReadDTO;
import com.pard.namukkun.user.dto.*;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    @Operation(summary = "유저 정보 전달", description = "로그인했을때 로컬에 저장하고, 로그아웃 하면 로컬에서 지우세요." + "기본적인 유저 정보를 전달합니다")
    public UserLoginInfoDTO getUserInfo(
            @RequestParam("userid") Long userId // debug
    ) {
        if (userId == null) return null;
        return userService.getUserLoginInfoDTO(userId);
    }
    @GetMapping("/info/all")
    @Operation(summary = "유저 주요 정보", description = "마이페이지에서 사용될 유저 정보를 전달합니다")
    public UserReadDTO getUserInfoAll(
            @RequestParam("userid") Long userId // debug
    ) {
        if (userId == null) return null;
        return userService.getUserInfoAll(userId);
    }

    @PatchMapping("/update")
    @Operation(summary = "유저 지역, 닉네임 변경", description = "유저의 아이디값, 변경할 닉네임, 지역정보를 통해 유저를 수정합니다.")
    public ResponseEntity<?> updateUser(
            @RequestParam("userid") Long userid, // debug
            @RequestBody UserUpdateDTO dto) {

        // 권한 확인
        if (!userid.equals(dto.getId())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        userService.updateUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//
//    @GetMapping("/posts")
//    @Operation(summary = "유저 닉네임 변경", description = "유저의 아이디값,변경할 닉네임을 통해 유저를 수정합니다.")
//    public UserPostDTO getUserPosts(@RequestParam("id") Long id) { // debug 용
//        return userService.getUserPosts(id);
//
//    }

    // 일단 없는 기능...?
    @DeleteMapping("/delete")
    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다")
    public ResponseEntity<?> deleteUser(@RequestParam("userid") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("")
//    public List<UserReadDTO> findAllUser() {
//        return userService.findAllUser();
//    }
}
