package com.pard.namukkun.oauth2.service;

import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final KakaoService kakaoService;
    private final LoginService loginService;
    private final HttpServletRequest request;
    private final UserService userService;

    public CustomOAuth2UserService(KakaoService kakaoService, LoginService loginService, UserService userService, HttpServletRequest request) {
        this.kakaoService = kakaoService;
        this.loginService = loginService;
        this.request = request;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        Long oauthId = userInfo.getId();
        if (userService.checkSigned(oauthId)) {
            loginService.signIn(request, oauthId);
        } else {
            loginService.signUp(request, userInfo, oauthId);
        }

        return oAuth2User;
    }
}