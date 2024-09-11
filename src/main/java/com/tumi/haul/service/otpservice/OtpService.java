package com.tumi.haul.service.otpservice;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OtpService {
    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private final ConcurrentHashMap<String, String>otpStore = new ConcurrentHashMap<>();

    public String generatedOTPToDmd(String key){
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i=0; i<OTP_LENGTH; i++){
            otp.append(random.nextInt(10));
        }
        otpStore.put(String.valueOf(key), otp.toString());
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
