package com.infinitycart.service.impl;

import com.infinitycart.config.JwtProvider;
import com.infinitycart.domain.USER_ROLE;
import com.infinitycart.model.Cart;
import com.infinitycart.model.Seller;
import com.infinitycart.model.User;
import com.infinitycart.model.VerificationCode;
import com.infinitycart.repository.CartRepository;
import com.infinitycart.repository.SellerRepository;
import com.infinitycart.repository.UserRepository;
import com.infinitycart.repository.VerificationCodeRepository;
import com.infinitycart.response.AuthResponse;
import com.infinitycart.response.LoginRequest;
import com.infinitycart.response.SignupRequest;
import com.infinitycart.service.AuthService;
import com.infinitycart.service.EmailService;
import com.infinitycart.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;


    @Override
    public String createUser(SignupRequest req) throws Exception {
        System.out.println("Email received: [" + req.getEmail() + "]");
        System.out.println("OTP received: [" + req.getOtp() + "]");


        // Find OTP by email
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

        // Validate OTP
        if (verificationCode == null) {
            throw new Exception("OTP not found. Please request a new OTP.");
        }

        if (!verificationCode.getOtp().trim().equals(req.getOtp().trim())) {
            throw new Exception("Invalid OTP");
        }

        // Check if user already exists
        User user = userRepository.findByEmail(req.getEmail());

        if (user == null) {
            User createdUser = new User();
            createdUser.setFullName(req.getFullName());
            createdUser.setEmail(req.getEmail());

            // Save the user's password, NOT the OTP
            createdUser.setPassword(passwordEncoder.encode(req.getPassword()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        // Delete OTP after successful verification
        verificationCodeRepository.delete(verificationCode);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception{
        String SIGNING_PREFIX="signing_";
        String SELLER_PREFIX="seller_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_CUSTOMER)){

                User user = userRepository.findByEmail(email);
                if(user == null) {
                    throw new Exception("User not found with the Provided email");
                }
            }else{
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null){
                    throw new Exception("Seller not found with the Provided email");
                }
            }


        }
        VerificationCode isExist= verificationCodeRepository.findByEmail(email);
        if(isExist != null) {
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);
        String subject = "Infinity Cart Login/SignUp OTP";
        String text = "We detected a login attempt from a new device or location. To verify your identity, please enter the following one-time password: " + otp+
                "" +
                ". This code is strictly confidential and will expire in 5 minutes. If you did not initiate this request, your account credentials may be compromised. Please update your password immediately.";

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }


    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getOtp();
        String otp = loginRequest.getOtp();

        Authentication authentication = authenticate(username, otp);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login successful");

        Collection<? extends  GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        authResponse.setRole(USER_ROLE.valueOf(roleName));
        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid UserName");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("Invalid OTP");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }


}
