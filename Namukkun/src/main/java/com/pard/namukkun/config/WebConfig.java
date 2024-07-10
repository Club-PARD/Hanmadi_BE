package com.pard.namukkun.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${server.domain}")
    private String serverDomain;

    @Value("${server.serviceDomain}")
    private String serviceDomain;



    @Override
    public void addCorsMappings(CorsRegistry registry) {

        @Value("${kakao.client_secret_id}") String FrontServer;

        registry.addMapping("/**")
                .allowedOrigins(
                        serverDomain,
                        serviceDomain
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Private-Network")
                .allowCredentials(true);


    }
}