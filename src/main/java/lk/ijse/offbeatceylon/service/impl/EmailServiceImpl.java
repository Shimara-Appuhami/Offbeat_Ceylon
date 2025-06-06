package lk.ijse.offbeatceylon.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.offbeatceylon.service.EmailService;
import lk.ijse.offbeatceylon.service.OtpService;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final OtpService otpService;

    public EmailServiceImpl(JavaMailSender mailSender, OtpService otpService) {
        this.mailSender = mailSender;
        this.otpService = otpService;
    }
    @Override
    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    public void sendOtpEmail(String to) {
        String otp = generateOTP();
        otpService.storeOtp(to, otp);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setTo(to);
            helper.setSubject("Your OTP Code");
            helper.setText("Your OTP code is: " + otp);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }
}