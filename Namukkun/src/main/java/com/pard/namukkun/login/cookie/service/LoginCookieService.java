package com.pard.namukkun.login.cookie.service;

import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginCookieService {

    public void createCookie(HttpServletResponse response, KakaoUserInfoResponseDto kakaoUserInfoResponseDto) {
        Cookie cookie = new Cookie("id", String.valueOf(kakaoUserInfoResponseDto.getId())); // Name-Value로 쿠키를 만듦
        cookie.setPath("/"); //
        //cookie.setSecure(true); //https 사용한다면 켜주세요
        cookie.setAttribute("SameSite", "Lax"); //이거는 나중에 설명
        cookie.setMaxAge(30 * 60); //이거는 몇 초동안 쿠키를 유지할 것인지
        cookie.setHttpOnly(true); // JavaScript에서 쿠키에 접근할 수 없도록 함
        response.addCookie(cookie);
    }
    public void deleteCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("id", null);
        cookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        cookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(cookie);
    }
}
