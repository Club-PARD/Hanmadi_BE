package com.pard.namukkun.login.controller;

import com.pard.namukkun.login.cookie.service.LoginCookieService;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import com.pard.namukkun.user.service.UserService;
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

    private final LoginService loginService;
    @Value("${kakao.client_id}")
    private String client_id; // client id

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    private final UserService userService;
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
//            return (String) sessionService.getSessionData(cookieId);

            return null;
        }
    }

    // 로그인 완료 했을떄 오는 경로
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<?> callback(HttpServletResponse response, @RequestParam("code") String code) {
        log.info("들어옴");
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info
        Long oauthId = userInfo.getId();

        if (userService.checkSigned(oauthId)) {// login
            loginService.signIn(oauthId);
            return new ResponseEntity<>(HttpStatus.OK); // 200
            // sign up
        } else {


            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        }
    }


    // 회원가입
    @PostMapping("/create/user")
    public ResponseEntity<?> createUser(HttpServletResponse response, @RequestParam("code") String code,
                                        @RequestParam("local") Integer local) {
        log.info("회원가입");
        log.info(code);
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info
        Long oauthId = userInfo.getId();

        // 잘못된 접근
        if (userService.checkSigned(oauthId)) return new ResponseEntity<>(HttpStatus.CONFLICT);

        // 유저 생성
        UserCreateDTO user = new UserCreateDTO(
                userInfo.getId(),
                userInfo.getKakaoAccount().getProfile().getNickName(),
                userInfo.getKakaoAccount().getProfile().getProfileImageUrl(),
                userInfo.getKakaoAccount().getEmail(),
                local
        );

        // 유저 저장
        userService.createUser(user);
        Long userId = userService.findUserByOauth(oauthId).getUserId();

        String randomId = sessionService.createRandomKey();
        loginCookieService.createCookie(response, randomId);

//        sessionService.addSessionData(randomId, userId);

        return new ResponseEntity<>(HttpStatus.OK); // 200
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletResponse response,
                                    @CookieValue(name = "id", required = true) String id) {

        loginCookieService.deleteCookie(response);
//        loginSessionSe
//        loginsession


        return new ResponseEntity<>(HttpStatus.OK); // 200
    }
}
