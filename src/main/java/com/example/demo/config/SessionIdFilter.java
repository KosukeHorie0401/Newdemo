package com.example.demo.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String sessionId = request.getHeader("X-Session-ID");
        System.out.println("Received X-Session-ID: " + sessionId);
        if (sessionId != null) {
            HttpSession session = request.getSession(false);
            if (session == null || !session.getId().equals(sessionId)) {
                session = request.getSession(true);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
            }
            Authentication auth = createAuthentication(session);
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Set authentication: " + auth);
            } else {
                System.out.println("Failed to create authentication");
            }
        } else {
            System.out.println("No X-Session-ID header found");
        }
        filterChain.doFilter(request, response);
    }

    private Authentication createAuthentication(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        if (userId != null && userRole != null) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole));
            return new UsernamePasswordAuthenticationToken(userId, null, authorities);
        }
        return null;
    }
}