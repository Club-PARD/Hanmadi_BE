package com.pard.namukkun.oauth2;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.service.KakaoService;
import com.pard.namukkun.login.service.LoginService;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RestController
@RequiredArgsConstructor
public class OAuth2LoginController {
    private final LoginService loginService;
    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> callback(
        HttpServletRequest request,
        @RequestParam("code") String code
        ) {
        // 토큰 생성
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        // 유저 정보 가져옴
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info

        // 카카오 유저 oauth id
        Long oauthId = userInfo.getId();

        // 카카오 유저 아이디로 서비스에 가입되어 있는지 확인
        if (userService.checkSigned(oauthId)) {// login
            loginService.signIn(request, oauthId);
            return new ResponseEntity<>(HttpStatus.OK); // 200
        } else { // sign up
            loginService.signUp(request, userInfo, oauthId);
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }

        // 여기에 카카오 인증 코드를 받아서 토큰을 요청하고, 유저 정보를 처리하는 로직을 추가합니다.
//
//
//
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes().toString();
    }
}
