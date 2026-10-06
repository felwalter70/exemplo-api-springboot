package com.course.cursospring.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.course.cursospring.security.JWTUtil;

import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity  (prePostEnabled = true)
@RequiredArgsConstructor 
public class SecurityConfig {
    private static final String[] PUBLIC_MATCHERS = {
        "/api",
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
        "/api/user",
        "/login"
    };

    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    private AuthenticationManager authenticationManager;

    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http
            .getSharedObject(AuthenticationManagerBuilder.class);
        
        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder());
        
        this.authenticationManager = authenticationManagerBuilder.build();

        return http

        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        
        .authorizeHttpRequests(request -> request
            .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            .requestMatchers(PUBLIC_MATCHERS).permitAll()
            .anyRequest().authenticated()
        )

        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

        .build();
    }

    @Bean 
    CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean 
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
