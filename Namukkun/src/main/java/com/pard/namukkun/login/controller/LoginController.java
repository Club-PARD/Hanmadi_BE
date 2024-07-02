package com.pard.namukkun.login.controller;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.session.service.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    @Value("${kakao.client_id}")
    private String client_id; // client id

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private final KakaoService kakaoService;
    private final SessionService sessionService;
    private final LoginCookieService loginCookieService;


    // move to login page
    @GetMapping("")
    public String login(Model model, @CookieValue(name = "id", required = false) String id) {
        String cookieId = loginCookieService.getCookie(id);


        if (cookieId == null) {
            String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=" + redirect_uri;
            model.addAttribute("location", location);
            return location;
        } else {
            return (String) sessionService.getSessionData(cookieId);
        }
    }

    // 로그인 완료 했을떄 오는 경로
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<?> callback(HttpServletResponse response, @RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // getting token
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info


        // TODO 추가 정보 제공 페이지로 이동, 정보 받아오기
        // TODO : create user here



        String seesionId = sessionService.createRandomKey();

        loginCookieService.createCookie(response, seesionId);
        sessionService.addSessionData(seesionId, "test");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
