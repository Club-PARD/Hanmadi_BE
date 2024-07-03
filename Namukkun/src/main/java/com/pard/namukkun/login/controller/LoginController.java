package com.pard.namukkun.login.controller;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserUpdateDTO;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
//    @Value("${kakao.client_id}")
//    private String client_id; // client id
//
//    @Value("${kakao.redirect_uri}")
//    private String redirect_uri;

    private final UserService userService;
    private final KakaoService kakaoService;
    private final SessionService sessionService;
    private final LoginCookieService loginCookieService;

    @PostMapping("")
    public ResponseEntity<?> sessionCheck(HttpServletRequest request, @CookieValue(name = "id") String sessionId) {
        if (sessionService.sessionCheck(request, sessionId)) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 안돼요

    }

    // 로그인 완료 했을떄 오는 경로
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<?> callback(HttpServletResponse response, HttpServletRequest request, @RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info

        Long oauthId = userInfo.getId();
        String sessionId = sessionService.createSessionId();

        // cookie set
        loginCookieService.createCookie(response, sessionId);

        if (userService.checkSigned(oauthId)) {// login
            log.info("로그인");
            loginService.signIn(request, sessionId, oauthId);
            return new ResponseEntity<>(HttpStatus.OK); // 200
        } else { // sign up
            log.info("회원가입");
            loginService.signUp(request, userInfo, sessionId, oauthId);
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
    }

    // 회원가입 -> update local
    @GetMapping("/create/user")
    public ResponseEntity<?> createUser(HttpServletRequest request, HttpServletResponse response,
                                        @CookieValue(name = "id", required = true) String sessionId,
//                                        @CookieValue(name = "id", required = false) String sessionId,
                                        @RequestParam("local") Integer local) {

//        log.info(String.valueOf(request.getCookies().length));
//        Cookie[] cookie = request.getCookies();
//
//        for (var i : cookie) {
//            log.info(i.getName());
//            if (i != null && i.getAttribute("id") != null)
//                System.out.println(i.getAttribute("id"));
//
//        }
//        String sessionId = "";
        log.info("id inputted : " + sessionId);
        Long userId = sessionService.getUserSessionData(request, sessionId).getUserId();
        UserUpdateDTO dto = new UserUpdateDTO(userId, local, null);
        userService.updateUserLocal(dto);
        return new ResponseEntity<>(HttpStatus.OK); // 200
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletResponse response, HttpServletRequest request, @CookieValue(name = "id", required = true) String id) {
        loginCookieService.deleteCookie(response);
        sessionService.removeSession(request, id);
        return new ResponseEntity<>(HttpStatus.OK); // 200
    }
}