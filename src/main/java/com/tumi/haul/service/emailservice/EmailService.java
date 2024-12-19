package com.tumi.haul.service.emailservice;

import com.tumi.haul.model.user.User;
import com.tumi.haul.service.otpservice.OtpService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;
    private final OtpService otpService;
    @Autowired
    public EmailService(OtpService otpService) {
        this.otpService = otpService;
    }

    public void sendVerificationCode(String email, String otp) throws MessagingException, UnsupportedEncodingException {
        String fromAddress="edupablo72@gmail.com";
        String companyName="Axle-ke";
        String subject="Your verification code";
        String content = "Thank you for registering with us! <br>"
                +"Your verification code is:<br>"
                +"<h3>[[otp]]</h3>";

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper= new MimeMessageHelper(message);
        messageHelper.setFrom(fromAddress,companyName);
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        content = content.replace("[[otp]]", otp);
        messageHelper.setText(content, true);
        emailSender.send(message);
    }
}
