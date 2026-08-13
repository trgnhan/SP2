package com.nhan.sp2.service;

import com.nhan.sp2.common.util.TokenType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String username, List<String> authorities);

    String generateRefreshToken(String username, List<String> authorities);

    String extractUsername(String token, TokenType tokenType);

}
