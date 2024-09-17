package com.tumi.haul.service.atsms;

import com.africastalking.AfricasTalking;
import com.africastalking.Logger;
import com.africastalking.SmsService;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class ATSmsService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ATSmsService.class);
    private static SmsService sms;
    @Autowired
    private static OtpService otpService = new OtpService();
    static String USERNAME = "sandbox";
    static String API_KEY = "atsk_c3e230fda784284809792a2e9d0a7bcb22954c71b8d1fc95da975dc8e6e8b30d33bd62a6";


public void setUpAfricasTalking(String phoneNumber, String otp)throws IOException{
        AfricasTalking.initialize(USERNAME, API_KEY);
        AfricasTalking.setLogger(new Logger() {
            @Override
            public void log(String message, Object... objects) {
                System.out.println(message);
            }
        });
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        log.info("got AT sms service");
    log.info("sent otp: {}", otp);

        //sms.send(otp,new String[]{phoneNumber}, true);

        log.info("recipient is: {}", phoneNumber);
    }


}
