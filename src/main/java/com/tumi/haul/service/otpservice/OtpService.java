package com.tumi.haul.service.otpservice;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.service.emailservice.EmailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private final ConcurrentHashMap<String, String>otpStore = new ConcurrentHashMap<>();
private EmailService emailService;

    public String generatedOTPToDmd(String key) throws MessagingException, UnsupportedEncodingException {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i=0; i<OTP_LENGTH; i++){
            otp.append(random.nextInt(10));
        }
        otpStore.put(key, otp.toString());
        log.info("This is the generated for:{}",otp + key);
        return otp.toString();
    }
/*
    public String generatedOTP(Email key){
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i=0; i<OTP_LENGTH; i++){
            otp.append(random.nextInt(10));
        }
        otpStore.put(String.valueOf(key), otp.toString());
        return otp.toString();
   }*/
    public boolean validateOTP(String key, String otp){
        String storedOTP = otpStore.get(key);
        if(storedOTP != null && storedOTP.equals(otp)){
            otpStore.remove(key);
            return true;
        }
        return false;

    }
}
