package com.infinitycart.controller;

import com.infinitycart.model.Seller;
import com.infinitycart.model.VerificationCode;
import com.infinitycart.response.ApiResponse;
import com.infinitycart.response.AuthResponse;
import com.infinitycart.response.LoginRequest;
import com.infinitycart.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SellerController {

    private final AuthService authService;


    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest reg) throws Exception {

        AuthResponse res = authService.login(reg);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Login successful");
        return ResponseEntity.ok(res);

    }



}
