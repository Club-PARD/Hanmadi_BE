package com.pard.namukkun.login.session.service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SessionService {

    public String createRandomKey(){
        return UUID.randomUUID().toString();
    }
//
//    public HttpSession getSession(String id){
//        return HttpSession
//    }


}
