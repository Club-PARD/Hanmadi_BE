package com.pard.namukkun.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // UrlBasedCorsConfigurationSource cors에 따라서 객체를 생성
        UrlBasedCorsConfigurationSource sourse =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); //frontEnd에서 axios로 처리 가능하게 만들겠다
        config.addAllowedOrigin("*"); //모든 ip에 응답을 허용하겠다
        // 따른 orgin에서오는 것을 허락함
        config.addAllowedHeader("*"); //모든 header에 응답을 허용하겠다
        config.addAllowedMethod("*"); //모든 post,get,put,delete,patch 요청을 허용하겠다
        // 모든 메서드에 대한접근을 허용함
        sourse.registerCorsConfiguration("*", config); //api로 들어오는 모든 요청은 이 config를 따르겠다
        // 위에서 만든 규칙들을 sourse에 적용시켜줌
        return new CorsFilter(sourse);
    }
}
