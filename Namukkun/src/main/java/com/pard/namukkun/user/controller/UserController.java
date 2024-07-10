package com.pard.namukkun.user.controller;

import com.pard.namukkun.login.session.DTO.UserSessionData;
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
import org.springframework.web.multipart.MultipartFile;

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
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        if (userId == null) return null;
        return userService.getUserLoginInfoDTO(userId);
    }

    @GetMapping("/info/all")
    @Operation(summary = "유저 주요 정보", description = "마이페이지에서 사용될 유저 정보를 전달합니다")
    public ResponseEntity<?> getUserInfoAll(
            @SessionAttribute(name = "userid", required = false) Long userId
    ) {
        if (userId == null) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        UserReadDTO dto = userService.getUserInfoAll(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/update")
    @Operation(summary = "유저 지역, 닉네임 변경", description = "유저의 아이디값, 변경할 닉네임, 지역정보를 통해 유저를 수정합니다.")
    public ResponseEntity<?> updateUser(
            @SessionAttribute(name = "userid", required = false) Long userId,
            @RequestBody UserUpdateDTO dto
    ) {

        // 권한 확인
        if (!userId.equals(dto.getId())) return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        userService.updateUser(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // -------------- 준현 수정 ----------------
    @PatchMapping("/update/profile")
    @Operation(summary = "유저 프로필 업데이트")
    public ResponseEntity<?> updateProfile(
            @RequestParam String profileImage,
            @RequestParam Long userId) {
        return userService.updateUserProfile(profileImage, userId);
    }
    // -------------- 준현 수정 ----------------

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
