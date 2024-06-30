package com.pard.namukkun.login.cookie.controller;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cookie")
@RequiredArgsConstructor
public class LoginCookieController {
    private final LoginCookieService loginCookieService;

//    @GetMapping("/cookie") //cookie 설정
//    public void setCookie(HttpServletResponse response, KakaoUserInfoResponseDto kakaoUserInfoResponseDto) {
//        loginCookieService.createCookie(response, kakaoUserInfoResponseDto);
//    }

//    @GetMapping("/get_cookie")
    public String getCookie(@CookieValue String id) {
        return id;
    }

    @PostMapping("/delete_cookie")
    public void deleteCookie(HttpServletResponse response) {
        loginCookieService.deleteCookie(response);
    }
}
