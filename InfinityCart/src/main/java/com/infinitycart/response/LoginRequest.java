package com.infinitycart.response;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String otp;
}
