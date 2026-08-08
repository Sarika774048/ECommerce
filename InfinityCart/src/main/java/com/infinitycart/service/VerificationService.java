package com.infinitycart.service;

import com.infinitycart.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface VerificationService extends JpaRepository<VerificationCode, Long> {
    VerificationCode createVerificationCode(String otp, String email);
}
