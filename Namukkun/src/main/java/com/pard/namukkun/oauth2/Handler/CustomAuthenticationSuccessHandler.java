package com.pard.namukkun.oauth2.Handler;

import com.pard.namukkun.login.session.DTO.UserSessionDTO;
import com.pard.namukkun.login.session.service.SessionService;
import com.pard.namukkun.user.entity.User;
import com.pard.namukkun.user.repo.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final SessionService sessionService;
    private final UserRepo userRepo;

    public CustomAuthenticationSuccessHandler(SessionService sessionService, UserRepo userRepo) {
        this.sessionService = sessionService;
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Long oauthId = Long.valueOf(oAuth2User.getAttributes().get("id").toString());

            User user = userRepo.findByOauthID(oauthId);
            if (user != null) {
                UserSessionDTO userSessionDTO = new UserSessionDTO(user);
                sessionService.addSessionData(request, userSessionDTO);
            }
        }

        response.sendRedirect("/");
    }
}
