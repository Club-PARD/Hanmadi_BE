package com.pard.namukkun.login.controller;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.login.session.DTO.UserSessionDTO;
import com.pard.namukkun.login.session.DTO.UserSessionData;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserUpdateDTO;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

//
//    @GetMapping("/test/send")
//    public void testpost(HttpServletRequest request, @RequestParam("code") String code) {
//        HttpSession session = request.getSession(true); // 없으면 새로 만들어요
//        session.setAttribute("test", code);
//        log.info("post들어옴");
////        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @GetMapping("/test")
//    public void testget(@SessionAttribute(value = "test",required = false) String str) {
//        log.info("get들어옴");
//        log.info(str);
////        return new ResponseEntity<>(HttpStatus.OK);
//    }


    @PostMapping("")
    public ResponseEntity<?> sessionCheck(

            @SessionAttribute(name = "userinfo", required = false)
            UserUpdateDTO dto

    ) {
        if (dto == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 안돼요
        else {
            log.info(dto.getNickName());
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    // 로그인 완료 했을떄 오는 경로

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<?> callback(
//            HttpServletResponse response,
            HttpServletRequest request,
            @RequestParam("code") String code) {


        log.info("로그인 들어옴");
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info

        Long oauthId = userInfo.getId();
//        String sessionId = sessionService.createSessionId();

        // cookie set
//        loginCookieService.createCookie(response, sessionId);

        if (userService.checkSigned(oauthId)) {// login
            log.info("로그인");
            loginService.signIn(request, oauthId);
            return new ResponseEntity<>(HttpStatus.OK); // 200
        } else { // sign up
            log.info("회원가입");
            loginService.signUp(request, userInfo, oauthId);
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
    }

    // 회원가입 -> update local
    @GetMapping("/create/user")
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public ResponseEntity<?> createUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @SessionAttribute(name = "userinfo", required = false) UserSessionData data,
            @RequestParam("local") Integer local
    ) {

        if (data == null) {
            log.error("data is null");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserSessionDTO dto = new UserSessionDTO(data);
        data = null;

        log.info("id inputted : {}", dto.getUserId());
        Long userId = sessionService.getUserSessionData(request).getUserId();

        UserUpdateDTO updateDto = new UserUpdateDTO(userId, local, null);
        userService.updateUserLocal(updateDto);

        return new ResponseEntity<>(HttpStatus.OK); // 200
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletRequest request) {
//        loginCookieService.deleteCookie(response);
        sessionService.removeSession(request);
        return new ResponseEntity<>(HttpStatus.OK); // 200
    }
}