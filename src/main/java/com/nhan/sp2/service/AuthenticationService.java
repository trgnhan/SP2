package com.nhan.sp2.service;

import com.nhan.sp2.dto.request.SignInRequest;
import com.nhan.sp2.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    TokenResponse getAccessToken(SignInRequest request);
    TokenResponse getRefreshToken(String request);

}
