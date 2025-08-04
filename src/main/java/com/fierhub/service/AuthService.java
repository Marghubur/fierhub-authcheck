package com.fierhub.service;

import com.fierhub.configures.RouteValidator;
import com.fierhub.model.CurrentSession;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

public class AuthService extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;
    @Value("${fierhub.enable.databaseConfiguration:false}")
    boolean databaseConfiguration;
    @Autowired
    RouteValidator routeValidator;
    @Autowired
    FierhubService fierhubService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService() {
    }

    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(String.valueOf(request.getMethod()))) {
            filterChain.doFilter(request, response);
        } else if (this.routeValidator.isSecured.test(request)) {
            filterChain.doFilter(request, response);
        } else {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwtToken = authHeader.substring(7);
                CurrentSession session = this.jwtService.getCurrentSession(jwtToken);
                if (session.getEmail() != null && SecurityContextHolder.getContext().getAuthentication() == null && !session.isExpired()) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(session.getEmail(), (Object)null, session.getRoles());
                    authToken.setDetails((new WebAuthenticationDetailsSource()).buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authorities: " + authToken.getAuthorities());
                }

                if (this.databaseConfiguration) {
                    try {
                        this.fierhubService.configureConnection();
                    } catch (Exception var8) {
                        throw new RuntimeException(var8);
                    }
                }

                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    private void showHeaders(HttpServletRequest httpRequest) {
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        logger.info("Headers in request call.");

        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            String headerValue = httpRequest.getHeader(headerName);
            logger.info(headerName + ":    ->  " + headerValue);
        }

    }
}

