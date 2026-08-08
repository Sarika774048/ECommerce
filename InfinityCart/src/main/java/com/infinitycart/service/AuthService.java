package com.infinitycart.service;

import com.infinitycart.domain.USER_ROLE;
import com.infinitycart.response.AuthResponse;
import com.infinitycart.response.LoginRequest;
import com.infinitycart.response.SignupRequest;

public interface AuthService {

    String createUser(SignupRequest req) throws Exception;

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    AuthResponse login(LoginRequest loginRequest);
}
