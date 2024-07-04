package com.pard.namukkun.login.service;

import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.session.DTO.UserSessionDTO;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.repo.UserRepo;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserRepo userRepo;


    // login
    public void signIn(HttpServletRequest request, Long oauthID) {
        UserSessionDTO dto = new UserSessionDTO(userRepo.findByOauthID(oauthID));
        sessionService.addSessionData(request, dto);
    }

    // 회원가입 - 유저 생성은 완료된 상태. 지역 설정
    public void signUp(HttpServletRequest request, KakaoUserInfoResponseDto userInfo, Long oauthId) {
        // 유저 생성
        UserCreateDTO user = new UserCreateDTO(
                userInfo.getId(),
                userInfo.getKakaoAccount().getProfile().getNickName(),
                userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                userInfo.getKakaoAccount().getEmail(),
                0
        );
        userService.createUser(user);

        // 세션 세팅
        Long userId = userService.findUserByOauth(oauthId).getUserId();
        UserSessionDTO dto = new UserSessionDTO(user, userId);
        sessionService.addSessionData(request, dto);
    }


//    //logout
//    public void logOut(HttpServletResponse response, String id) {
//        loginCookieService.deleteCookie(response);
//        sessionService.removeSession(id);
//    }
}
