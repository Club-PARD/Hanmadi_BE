package com.pard.namukkun.login.service;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.session.DTO.SessionUserDTO;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final SessionService sessionService;
    private final LoginCookieService loginCookieService;


    // login
    public void signIn(Long oauthID) {

    }

    public void setCookie(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, Long userId) {

//        String randomId = sessionService.createRandomKey();
//        loginCookieService.createCookie(httpServletResponse, randomId);
//
//        SessionUserDTO dto = new SessionUserDTO(user, userId);
//
//        sessionService.addSessionData(httpServletRequest, randomId, dto);

    }


    // 회원가입 - 유저 생성은 완료된 상태. 지역 설정
    public void signUp(KakaoUserInfoResponseDto userInfo, Long oauthId) {

        // 유저 생성
        UserCreateDTO user = new UserCreateDTO(
                userInfo.getId(),
                userInfo.getKakaoAccount().getProfile().getNickName(),
                userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                userInfo.getKakaoAccount().getEmail(),
                0
        );

        userService.createUser(user);
        Long userId = userService.findUserByOauth(oauthId).getUserId();

    }

    //logout
    public void logOut(HttpServletResponse response, String id) {
        loginCookieService.deleteCookie(response);
        sessionService.removeSession(id);
    }
}
