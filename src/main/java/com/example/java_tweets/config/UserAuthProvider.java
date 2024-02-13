package com.example.java_tweets.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.java_tweets.models.dtos.response.UserDTO;
import com.example.java_tweets.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.Base64;

@Component
public class UserAuthProvider {

    @Value("${tweets.jwt.secret}")
    private String secret;
    private final UserService userService;

    public UserAuthProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserDTO userDTO) {
        Date dateNow = new Date();
        Date validity = new Date(dateNow.getTime() + 3600000);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withSubject(userDTO.getEmail())
                .withIssuedAt(dateNow)
                .withExpiresAt(validity)
                .withClaim("name", userDTO.getName())
                .sign(algorithm);

    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(decodedJWT.getSubject());
        userDTO.setEmail(decodedJWT.getClaim("name").asString());

        return new UsernamePasswordAuthenticationToken(userDTO, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        UserDTO userDTO = userService.findByLogin(decoded.getSubject());

        return new UsernamePasswordAuthenticationToken(userDTO, null, Collections.emptyList());
    }


}
