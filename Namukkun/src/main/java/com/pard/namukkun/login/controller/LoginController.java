package com.pard.namukkun.login.controller;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.login.session.DTO.SessionUserDTO;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.dto.UserUpdateDTO;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    @Value("${kakao.client_id}")
    private String client_id; // client id

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private final UserService userService;
    private final KakaoService kakaoService;
    private final SessionService sessionService;
    private final LoginCookieService loginCookieService;

    // 로그인 완료 했을떄 오는 경로
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<?> callback(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info

        Long oauthId = userInfo.getId();

        if (userService.checkSigned(oauthId)) {// login
            log.info("로그인");

            loginService.signIn(oauthId);
            return new ResponseEntity<>(HttpStatus.OK); // 200
        } else { // sign up
            log.info("회원가입");

            // 유저 생성
            UserCreateDTO user = new UserCreateDTO(
                    userInfo.getId(),
                    userInfo.getKakaoAccount().getProfile().getNickName(),
                    userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                    userInfo.getKakaoAccount().getEmail(),
                    0
            );

            userService.createUser(user);


            // 쿠키, 세션 세팅
            Long userId = userService.findUserByOauth(oauthId).getUserId();


            String randomId = sessionService.createRandomKey();
            loginCookieService.createCookie(httpServletResponse, randomId);

            SessionUserDTO dto = new SessionUserDTO(user, userId);

            sessionService.addSessionData(httpServletRequest, randomId, dto);

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        }
    }

    // 회원가입 -> update local
    @PostMapping("/create/user")
    public ResponseEntity<?> createUser(
            HttpServletRequest request,
            @CookieValue(name = "id", required = false) String id,
            @RequestParam("local") Integer local
    ) {
        Long userId = sessionService.getSessionData(request, id).getUserId();

        Cookie[] cookies = request.getCookies();

        for (var cookie : cookies) {
            if (cookie.getAttribute("id") != null) {
                System.out.println("찾았어요");
            }
        }


        UserUpdateDTO dto = new UserUpdateDTO(userId, local, null); // nickname 변경 상황 아니라 null 처리
        try {
            userService.updateUserLocal(dto);
        } catch (Exception e) {
            log.info("sign up error");
        }

        return new ResponseEntity<>(HttpStatus.OK); // 200
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletResponse response, @CookieValue(name = "id", required = true) String id) {
        loginCookieService.deleteCookie(response);
        sessionService.removeSession(id);

        //        loginService.logOut(response, id);
        return new ResponseEntity<>(HttpStatus.OK); // 200
    }
}
