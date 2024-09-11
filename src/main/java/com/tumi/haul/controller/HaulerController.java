package com.tumi.haul.controller;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Hauler;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.service.HaulerService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/auth")
public class HaulerController{
    private static final Logger log = LoggerFactory.getLogger(HaulerController.class);
    private OtpService otpService;
    @Autowired
    private JWTService jwtService;
    private final HaulerService haulerService;

    public HaulerController(HaulerService haulerService) {
        this.haulerService = haulerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?>registerUser(@RequestBody Hauler hauler){
        try{
            haulerService.createUser(hauler);
            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

        }

    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@RequestParam PhoneNumber phoneNumber, @RequestParam String otp){
        try{
            haulerService.verifyOTP(phoneNumber, otp);
            return ResponseEntity.status(200).body(jwtService.generateToken(phoneNumber));
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
        }
    }
    @PostMapping("/login")
    public String login(@RequestBody Hauler hauler){

        try{
            haulerService.verify(hauler);
            //  emailService.sendVerificationCode(client);
            //String otp = otpService.generatedOTP(client.getEmail());
            //String otp = otpService.generatedOTPToDmd(client.getPhoneNumber().getValue());
            //aTsmsSender.sendOTP(client.getPhoneNumber().getInternationalFormat(), otp);

            log.info("sent OTP to:{}", hauler.getPhoneNumber());
            return "We have sent OTP to:{}" + hauler.getPhoneNumber().getValue();

        }catch (Exception e){

            log.warn(e.getLocalizedMessage());
            return "Failed";
        }

    }
}
