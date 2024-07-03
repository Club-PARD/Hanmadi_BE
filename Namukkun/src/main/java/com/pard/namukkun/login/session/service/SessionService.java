package com.pard.namukkun.login.session.service;

import com.pard.namukkun.Data;
import com.pard.namukkun.login.session.DTO.UserSessionDTO;
import com.pard.namukkun.login.session.DTO.UserSessionData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class SessionService {
    public String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public Boolean sessionCheck(HttpServletRequest request, String sessionId) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(sessionId) != null;
        }
        return false;
    }

    public void addSessionData(HttpServletRequest request, String sessionId, UserSessionDTO dto) {
        HttpSession session = request.getSession(true);
        UserSessionData data = new UserSessionData(dto);

        session.setAttribute(sessionId, data);
        session.setMaxInactiveInterval(Data.cookieSessionTime);
        log.info(sessionId);
        log.info("세션 생성 완료");
    }

    public UserSessionDTO getUserSessionData(HttpServletRequest request, String sessionId) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserSessionData data = (UserSessionData) session.getAttribute(sessionId);
            if (data != null) {
                return new UserSessionDTO(data);
            }
        }
        return null;
    }

    public void removeSession(HttpServletRequest request, String sessionId) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(sessionId);
            session.invalidate();
        }
    }
}

