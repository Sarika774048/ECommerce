package com.infinitycart.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
           MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
           mimeMessageHelper.setFrom("sarikanmks9880@gmail.com");
           mimeMessageHelper.setTo(userEmail);
           mimeMessageHelper.setSubject(subject);
           mimeMessageHelper.setText(text);
           javaMailSender.send(message);

        } catch (MailException | MessagingException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }


}
