package com.pard.namukkun.login.controller;


import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        System.out.println(code);
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
//        System.out.println(userInfo.getKakaoAccount().getEmail());
        // User 로그인, 또는 회원가입 로직 추가

        userService.createUser(new UserCreateDTO(userInfo.getId(), userInfo.getKakaoAccount().getEmail(), userInfo.getKakaoAccount().getName()));


        return new ResponseEntity<>(HttpStatus.OK);
        }


}
