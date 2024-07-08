package com.pard.namukkun.login.controller;

import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.login.session.DTO.UserSessionData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    // 로그인 완료 했을떄 오는 경로
    @GetMapping("/oauth2/code/kakao")
    @Operation(summary = "로그인 완료 경로", description = "카카오 oauth 인가코드를 함꼐 전송하여 로그인, 회원가입합니다")
    public ResponseEntity<?> callback(
            HttpServletRequest request,
            @RequestParam("code") String code) {

        return loginService.oauthLogin(request, code);
    }

    // 회원가입 -> update local
    @GetMapping("/create/user")
    @Operation(summary = "회원가입시 유저 로컬을 설정합니다", description = "로컬지역을 전달하여 유저 지역을 설정합니다")
    public ResponseEntity<?> createUser(
            HttpServletRequest request,
            @SessionAttribute(name = "userinfo", required = false) UserSessionData data,
            @RequestParam("local") Integer local
    ) {
        return loginService.signUpLocalSet(request, data, local);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 합니다 (세션 삭제)")
    public ResponseEntity<?> logOut(
            HttpServletRequest request,
            @SessionAttribute(name = "userinfo", required = false) UserSessionData data
    ) {

        return loginService.logOut(request, data);
    }
}