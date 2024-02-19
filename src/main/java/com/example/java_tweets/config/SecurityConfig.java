package com.example.java_tweets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserAuthEntryPoint userAuthEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserAuthEntryPoint userAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userAuthEntryPoint = userAuthEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthEntryPoint))
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.POST, "/api/user/login", "/api/user/create-user").permitAll()
                        .anyRequest().authenticated());
        return httpSecurity.build();
    }
}
