package com.pard.namukkun.config;

import com.pard.namukkun.oauth2.CustomAuthenticationSuccessHandler;
import com.pard.namukkun.oauth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    private final  Custo
//    private CustomUserDetailsService customUserDetailsService;

//    @Autowired
//    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
//        this.customUserDetailsService = customUserDetailsService;
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        // 허용
//                        // 글 읽기 허용
//                        .requestMatchers("/post/read/**").permitAll()
//                        // 포스트잇 읽기 허용
//                        .requestMatchers("/post/postit/read").permitAll()
//                        // 덧글 읽기 허용
//                        .requestMatchers(HttpMethod.GET, "/post/comment/**").permitAll()
//                        // 로그인, 회원 가입 허용
                        .requestMatchers("/login/oauth2/code/kakao").permitAll()
//
//                        // 제한
//                        // 위에 허용한 것 외의 모든 포스트 요청 인증 필요
//                        .requestMatchers("/post/**").authenticated()
//                        // 위에 허용한 것 외의 모든 포스트잇 요청 인증 필요
//                        .requestMatchers("/post/postit/**").authenticated()
//                        // 위에 허용한 것 외의 모든 댓글 요청 인증 필요
//                        .requestMatchers("/post/comment/**").authenticated()
//                        // 로그인 페이지 인증 필요
                        .requestMatchers("/login/**").authenticated()
//                        // 유저 관련 요청 인증 필요
//                        .requestMatchers("/user/**").authenticated()

                        // 그 외의 모든 요청은 접근 허용
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);  // CSRF 보호 비활성화 (개발 중에만)
//                .userDetailsService(customUserDetailsService);
        return http.build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

//    @Bean
//    public HttpSessionStrategy httpSessionStrategy() {
//        return new HeaderHttpSessionStrategy();
//    }
}