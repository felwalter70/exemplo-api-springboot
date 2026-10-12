package com.course.cursospring.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.course.cursospring.domain.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;
import tools.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Usuário ou senha inválidos\"}");
        });
    }

    @Override 
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        User userCredentials;
        
        try {
            userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
        }
        catch (Exception e) {
            throw new AuthenticationServiceException("JSON de login inválido", e);
        }

        var authReq = UsernamePasswordAuthenticationToken.unauthenticated(userCredentials.getUsername(), userCredentials.getPassword());
        
        Authentication authentication = this.authenticationManager.authenticate(authReq);
        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain, Authentication authentication) throws java.io.IOException, ServletException {
            UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
            String username = userSpringSecurity.getUsername();
            String token = this.jwtUtil.generateToken(username);

            response.addHeader("Authorization", "Bearer " + token);
            response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }
}
