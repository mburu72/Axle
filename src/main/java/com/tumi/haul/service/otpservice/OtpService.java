package com.tumi.haul.service.otpservice;

import com.tumi.haul.security.jwt.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class OtpService {

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final Logger log = LoggerFactory.getLogger(OtpService.class);
    private final ConcurrentHashMap<String, String>otpStore = new ConcurrentHashMap<>();



    public String generatedOTPToDmd(String key){
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i=0; i<OTP_LENGTH; i++){
            otp.append(random.nextInt(10));
        }
        otpStore.put(key, otp.toString());

        log.info("This is the generated otp:{}",otp.toString());
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
        log.info("Generated OTP for : {}", key);
        String storedOTP = otpStore.get(key);
        log.info("this is the stored OTP:{}", storedOTP);
        if(storedOTP != null && storedOTP.equals(otp)){
            log.info("Done");
            otpStore.remove(key);
            return true;
        }
        log.info("not done");
        return false;

    }
}
