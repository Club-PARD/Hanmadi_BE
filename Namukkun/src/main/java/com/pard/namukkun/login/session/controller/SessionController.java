package com.pard.namukkun.login.session.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {


    @PostMapping("/session")
    public String createSession(HttpServletRequest req, String sessionKey) {



        // 세션이 존재할 경우 세션 반환, 없을 경우 새로운 세션을 생성한 후 반환
        HttpSession session = req.getSession(true);

        // 세션에 저장될 정보 Name - Value 를 추가합니다.
        session.setAttribute("session", "하이 세션");
        session.setAttribute("gkdl", "qkdl session");




//        session.setAttribute(sessionKey, );







        return "createSession";
    }

    @GetMapping("/get_session")
    public String getSession(HttpSession session) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 null 반환

        String value = (String) session.getAttribute("session"); // 가져온 세션에 저장된 Value 를 Name 을 사용하여 가져옵니다.
        String value2 = (String) session.getAttribute("gkdl");
        System.out.println("value = " + value);
        System.out.println("value = " + value2);

        return "getSession : " + value + value2;
    }

    @PostMapping("/delete_session")
    public String deleteSession(HttpServletRequest req) {
        // 세션이 존재할 경우 세션 반환, 없을 경우 새로운 세션을 생성한 후 반환
        HttpSession session = req.getSession(true);

        session.removeAttribute("session");

        return "createSession";
    }
}
