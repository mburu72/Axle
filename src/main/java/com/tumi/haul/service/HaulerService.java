package com.tumi.haul.service;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.Hauler;
import com.tumi.haul.repository.HaulerRepository;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HaulerService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final HaulerRepository haulerRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JWTService jwtService;

    public HaulerService(HaulerRepository haulerRepository) {
        this.haulerRepository = haulerRepository;
    }

    public void createUser(Hauler hauler) {

        if(haulerRepository.findByEmail(hauler.getEmail()).isPresent()){
            throw new IllegalArgumentException("User with this email already exists!");
        }
        if (haulerRepository.findByPhoneNumber(hauler.getPhoneNumber()).isPresent()){
            throw new IllegalArgumentException("This number is already registered!");
        }
        hauler.setPassword(encoder.encode(hauler.getPassword()));
        haulerRepository.save(hauler);

    }
    public String verify(Hauler hauler) throws Exception {
        Authentication auth =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(hauler.getPhoneNumber().getValue(), hauler.getPassword()));
        if (auth.isAuthenticated()){
            String otp=otpService.generatedOTPToDmd(hauler.getPhoneNumber().getValue());
            // aTsmsSender.sendOTP(client.getPhoneNumber().getInternationalFormat(),otp );

            // return
        }
        return "failed";
    }
    public String verifyOTP(String phoneNumber, String otp){
        if(otpService.validateOTP(phoneNumber, otp)){
            return "";
        }
        return "Invalid OTP";
    }

    public List<Hauler> getAllClients(){

        return haulerRepository.findAll();
    }


}

