package com.nhan.sp2.service.impl;

import com.nhan.sp2.dto.request.SignInRequest;
import com.nhan.sp2.dto.response.TokenResponse;
import com.nhan.sp2.repository.UserRepository;
import com.nhan.sp2.service.AuthenticationService;
import com.nhan.sp2.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.nhan.sp2.common.util.TokenType.REFRESH_TOKEN;

@Service
@Slf4j(topic = "AUTHENTICATION-SERVICE")
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        log.info("Get access token");

        List<String> authorities = new ArrayList<>();
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            log.info("isAuthenticated: {}", authenticate.isAuthenticated());
            log.info("Authorities: {}", authenticate.getAuthorities().toString());
            authorities.add(authenticate.getAuthorities().toString());

            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (AuthenticationException e) {
            log.error("Login fail!, message= {}",e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        var user = userRepository.findByUsername(request.getUsername());

        String accessToken =  jwtService.generateAccessToken(request.getUsername(),authorities);
        String refreshToken =  jwtService.generateRefreshToken(request.getUsername(), authorities);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        log.info("Get refresh token");

        try {
            String username = jwtService.extractUsername(request, REFRESH_TOKEN);

            var user = userRepository.findByUsername(username);
            if (user == null) {
                log.error("User not found for username: {}", username);
                throw new AccessDeniedException("User not found");
            }

            List<String> authorities = new ArrayList<>();

            if (user.getAuthorities() != null) {
                user.getAuthorities().forEach(authority ->
                        authorities.add(authority.getAuthority())
                );
            }

            String newAccessToken = jwtService.generateAccessToken(username, authorities);
            String newRefreshToken = jwtService.generateRefreshToken(username, authorities);

            return TokenResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();

        } catch (Exception e) {
            log.error("Get refresh token fail!, message= {}", e.getMessage());
            throw new AccessDeniedException("Invalid refresh token: " + e.getMessage());
        }
    }
}
