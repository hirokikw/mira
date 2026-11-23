package com.examplecompany.mira.common.security;

import com.examplecompany.mira.common.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public LoginSuccessHandler(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) throws IOException, ServletException {
        final String loginId = authentication.getName();
        userRepository
                .findByLoginId(loginId)
                .ifPresent(
                        user -> {
                            final OffsetDateTime now = OffsetDateTime.now();
                            user.setLastLoginDatetime(now);
                            user.setUpdateDatetime(now);
                        });
        response.sendRedirect(request.getContextPath() + "/menu");
    }
}