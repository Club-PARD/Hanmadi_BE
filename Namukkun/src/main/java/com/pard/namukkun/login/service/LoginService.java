package com.pard.namukkun.login.service;

import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import com.pard.namukkun.login.session.DTO.UserSessionDTO;
import com.pard.namukkun.login.session.DTO.UserSessionData;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.dto.UserCreateDTO;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import com.pard.namukkun.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserRepo userRepo;

    private final KakaoService kakaoService;

    // 카카오 인증 진행
    public ResponseEntity<?> oauthLogin(HttpServletRequest request, String code) {
        // 토큰 생성
        String accessToken = kakaoService.getAccessTokenFromKakao(code); // get token from kakao
        // 유저 정보 가져옴
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken); // get user info

        // 카카오 유저 oauth id
        Long oauthId = userInfo.getId();

        // 카카오 유저 아이디로 서비스에 가입되어 있는지 확인
        if (userService.checkSigned(oauthId)) {// login
            signIn(request, oauthId);
            return new ResponseEntity<>(HttpStatus.OK); // 200
        } else { // sign up
            signUp(request, userInfo, oauthId);
            return new ResponseEntity<>(HttpStatus.CREATED); // 201
        }
    }

    // login
    public void signIn(HttpServletRequest request, Long oauthID) {
        // 세션 데이터 받아오기
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
        // 유저 생성
        userService.createUser(user);

        // 세션 세팅
        Long userId = userService.findUserByOauth(oauthId).getUserId();
        UserSessionDTO dto = new UserSessionDTO(user, userId);
        sessionService.addSessionData(request, dto);
    }

    // 회원가입 이후 유저 지역 선택
    public ResponseEntity<?> signUpLocalSet(HttpServletRequest request, UserSessionData data, Integer local) {

        // 데이터 없으면 (세션 종료?)
        if (data == null) {
            log.error("data is null");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // 세션 데이터 받아오기
        UserSessionDTO dto = new UserSessionDTO(data);

        // 유저 아이디 받아오기
        Long userId = sessionService.getUserSessionData(request).getUserId();

        // 아이디로 유저 찾기
        User user = userRepo.findById(userId).orElseThrow();

        // 유저 지역 변경
        user.updateUserinfo(user.getNickName(), local, user.getProfileImage());

        // 유저 저장
        userRepo.save(user);
        return new ResponseEntity<>(HttpStatus.OK); // 200
    }

    public ResponseEntity<?> logOut(HttpServletRequest request, UserSessionData data) {
        sessionService.removeSession(request);
        return new ResponseEntity<>(HttpStatus.OK); // 200
    }
}
