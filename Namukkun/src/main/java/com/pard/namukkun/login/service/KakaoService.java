package com.pard.namukkun.login.service;


import com.pard.namukkun.login.dto.KakaoTokenResponseDto;
import com.pard.namukkun.login.dto.KakaoUserInfoResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
//@RequiredArgsConstructor
@Service
public class KakaoService {

    private String clientId;
    private String clientSecretId;

    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;


    @Autowired
    public KakaoService(@Value("${kakao.client_id}") String clientId,
                        @Value("${kakao.client_secret_id}") String clientSecretId) {
        this.clientSecretId = clientSecretId;
        this.clientId = clientId;
        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    public String getAccessTokenFromKakao(String code) {
        try {
            KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .path("/oauth/token")
                            .queryParam("grant_type", "authorization_code")
                            .queryParam("client_id", clientId)
                            .queryParam("client_secret", clientSecretId)
                            .queryParam("code", code)
                            .build(true))
                    .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                    .bodyToMono(KakaoTokenResponseDto.class)
                    .block();
            log.info("[Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
            log.info("[Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
            log.info("[Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());
            //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
            return kakaoTokenResponseDto.getAccessToken();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {

        try {

            KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .path("/v2/user/me")
                            .build(true))
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                    .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                    .retrieve()
                    //TODO : Custom Exception
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                    .bodyToMono(KakaoUserInfoResponseDto.class)
                    .block();

            log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
            log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
            log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

            return userInfo;
        } catch (Exception e) {
            return null;
        }
    }
}
