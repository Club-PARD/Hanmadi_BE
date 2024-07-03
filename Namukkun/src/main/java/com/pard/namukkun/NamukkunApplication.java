package com.pard.namukkun;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@SpringBootApplication
// ------------- 이거 로컬에서 할 때는 빼고 ------------//
//@OpenAPIDefinition(
//        servers = {
//                @Server(url="https://hanmadi.site", description = "Default Server url")
//        }
//)
// -----------------------------------------------//
public class NamukkunApplication {
    public static void main(String[] args) {
        SpringApplication.run(NamukkunApplication.class, args);
    }

}
