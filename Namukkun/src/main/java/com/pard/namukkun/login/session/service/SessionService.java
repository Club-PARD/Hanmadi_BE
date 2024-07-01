package com.pard.namukkun.login.session.service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class SessionService {
    HttpSession session;

    public String createRandomKey() {
        return UUID.randomUUID().toString();
    }

    
    //Todo test데이터 바꾸기
    public void addSessionData(String id, String test) {
        session.setAttribute(id, test);
        log.info(session.getAttribute(id).toString());
    }

    public Object getSessionData(String id){
        return session.getAttribute(id).toString();
    }

//
//    public HttpSession getSession(String id){
//        return HttpSession
//    }


}
