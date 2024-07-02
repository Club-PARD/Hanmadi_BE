package com.pard.namukkun.login.session.service;

import com.pard.namukkun.Data;
import com.pard.namukkun.login.session.DTO.SessionUserDTO;
import com.pard.namukkun.user.dto.UserReadDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.session.StandardSession;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class SessionService {
    private Map<String, HttpSession> sessionMap;


    public String createRandomKey() {
        return UUID.randomUUID().toString();
    }

    public void addSessionData(HttpServletRequest request, String sessionId, UserReadDTO dto) {
        HttpSession session = request.getSession(true);

        session.setAttribute("id", dto.getUserId());
        session.setAttribute("nickName", dto.getNickName());
        session.setAttribute("local", dto.getLocal());
        session.setAttribute("profileImage", dto.getProfileImage());

        session.setMaxInactiveInterval(Data.cookieSessionTime);

        sessionMap.put(sessionId, session);
    }

//    public SessionUserDTO getSessionData(HttpServletRequest request, String sessionId) {
//
//        sessionMap.get(sessionId).getAttribute("");
//        Long id = (Long) sessionMap.get(sessionId).getAttribute("id");
//        String nickName = (String) sessionMap.get(sessionId).getAttribute("nickName");
////        Integer local
//
//
////        SessionUserDTO dto = new SessionUserDTO();
//
//        return dto;
//    }

//    public Boolean removeSession(String sessionId){
//        session.removeAttribute(sessionId);
//        return true;
//    }
//    }
}

