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
//
//    public Boolean sessionCheck(HttpServletRequest request, String sessionId) {
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            return session.getAttribute(sessionId) != null;
//        }
//        return false;
//    }

    public void addSessionData(HttpServletRequest request, UserSessionDTO dto) {
        HttpSession session = request.getSession(true); // 없으면 새로 만들어요
        UserSessionData data = new UserSessionData(dto); // dto -> data

        session.setAttribute("userinfo", data); // session save
        session.setAttribute("userid", data.getUserId()); // session save
        session.setMaxInactiveInterval(Data.cookieSessionTime); // time set


        log.info("세션 생성 완료");
    }

//    public void addSessionData(HttpServletRequest request) {
//        HttpSession session = request.getSession(true); // 없으면 새로 만들어요
//
//        session.setAttribute("userid", ); // session save
//        session.setMaxInactiveInterval(Data.cookieSessionTime); // time set
//
//
//        log.info("세션 생성 완료");
//    }

    public UserSessionDTO getUserSessionData(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserSessionData data = (UserSessionData) session.getAttribute("userinfo");
            if (data != null) {
                return new UserSessionDTO(data);
            }
        }
        return null;
    }

    public void removeSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("userid");
            session.removeAttribute("userinfo");
            session.invalidate();
        }
    }
}

