package com.infinitycart.controller;

import com.infinitycart.domain.USER_ROLE;
import com.infinitycart.model.User;
import com.infinitycart.model.VerificationCode;
import com.infinitycart.repository.UserRepository;
import com.infinitycart.request.LoginOtpRequest;
import com.infinitycart.response.ApiResponse;
import com.infinitycart.response.AuthResponse;
import com.infinitycart.response.LoginRequest;
import com.infinitycart.response.SignupRequest;
import com.infinitycart.service.AuthService;
import com.infinitycart.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        String jwt = authService.createUser(req);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Registration Success");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest req) throws Exception {

        authService.sentLoginOtp(req.getEmail(), req.getRole());
        ApiResponse res = new ApiResponse();

        res.setMessage("Otp sent successfully!");
        return ResponseEntity.ok(res);

    }

    @PostMapping("/signing")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest reg) throws Exception {
        AuthResponse res = authService.login(reg);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Login successful");
        return ResponseEntity.ok(res);
    }


}
