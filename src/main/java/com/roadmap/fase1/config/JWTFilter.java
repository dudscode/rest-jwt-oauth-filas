package com.roadmap.fase1.config;


import com.roadmap.fase1.dto.UserTokenDTO;
import org.springframework.web.filter.GenericFilterBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;


public class JWTFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    private static final String[] AUTH_WHITELIST = {
            "/user/register",
            "/error",
            "/h2-console",
            "/user/me",
            "/user/login"

    };
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String path = httpServletRequest.getRequestURI();
        for (String whitelisted : AUTH_WHITELIST) {
            if (path.equals(whitelisted)) { // ou startsWith, depende da sua regra
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        String jwt = httpServletRequest.getHeader("Authorization");

        if (StringUtils.hasText(jwt)) {
            if (tokenProvider.isValid(jwt, servletResponse)) {
                final UserTokenDTO userDto = tokenProvider.getUserFromToken(jwt);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDto.getEmail(), 
                    null, 
                    Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
