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


    public void addSessionData(HttpServletRequest request, String sessionId, SessionUserDTO dto) {
        HttpSession session = request.getSession(true);

        session.setAttribute("id", dto.getUserId());
        session.setAttribute("nickName", dto.getNickName());
        session.setAttribute("local", dto.getLocal());
        session.setAttribute("profileImage", dto.getProfileImage());

        session.setMaxInactiveInterval(Data.cookieSessionTime);

        sessionMap.put(sessionId, session);
        log.info("세션 생성 완료");
    }

    public SessionUserDTO getSessionData(HttpServletRequest request, String sessionId) {
        Long id = (Long) sessionMap.get(sessionId).getAttribute("id");
        String nickName = (String) sessionMap.get(sessionId).getAttribute("nickName");
        Integer local = (Integer) sessionMap.get(sessionId).getAttribute("local");
        String profileImage = (String) sessionMap.get(sessionId).getAttribute("profileImage");
        SessionUserDTO dto = new SessionUserDTO(id, nickName, local, profileImage);
        return dto;
    }

    public Boolean removeSession(String sessionId) {
        if (sessionMap.get(sessionId) != null)
            sessionMap.get(sessionId).invalidate();
        return true;
    }
//    }
}

