package com.pard.namukkun.login.cookie.service;

import com.pard.namukkun.Data;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Slf4j
@Service
public class LoginCookieService {

//
//    public String getCookie(@CookieValue String id) {
//        return id;
//    }

//    public void createCookie(HttpServletResponse response, String id) {
//        Cookie cookie = new Cookie("id", id);
//
//
//        cookie.setPath("/"); // 사용범위  TODO 이후 로그인에만 사용하게 할 수 있을 듯
////        cookie.setSecure(true); //https 사용시
////        cookie.setAttribute("SameSite", "Lax"); // CSRF 공격 방지
//        cookie.setAttribute("SameSite", "None"); // CSRF 공격 방지
//        cookie.setMaxAge(Data.cookieSessionTime); // 쿠키 유지 시간
////        cookie.setDomain("hanmadi.site"); // 이 친구 설정하면 쿠키 안날아감. -> 이후 도메인 통일하고 사용해야하는 친구
////        cookie.setHttpOnly(true); // JS 접근 제어
//        response.addCookie(cookie);
//
//        log.info("쿠키 생성됨 : " + id);
//    }

//    public void deleteCookie(HttpServletResponse response) {
//        Cookie cookie = new Cookie("id", null);
//        cookie.setMaxAge(0); // 쿠키의 유지 시간을 0 으로 만들어 제거
//        cookie.setPath("/"); // 모든 경로에서 삭제
//        response.addCookie(cookie);
//    }
}
