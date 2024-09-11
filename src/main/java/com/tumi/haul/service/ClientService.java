package com.tumi.haul.service;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.repository.ClientRepository;
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
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JWTService jwtService;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createUser(Client client) {

        if(clientRepository.findByEmail(client.getEmail()).isPresent()){
            throw new IllegalArgumentException("User with this email already exists!");
        }
        if (clientRepository.findByPhoneNumber(client.getPhoneNumber()).isPresent()){
            throw new IllegalArgumentException("This number is already registered!");
        }
        client.setPassword(encoder.encode(client.getPassword()));
        clientRepository.save(client);

    }
    public String verify(Client client) throws Exception {
        Authentication auth =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(client.getPhoneNumber().getValue(), client.getPassword()));
        if (auth.isAuthenticated()){
            String otp=otpService.generatedOTPToDmd(client.getPhoneNumber().getInternationalFormat());
           // aTsmsSender.sendOTP(client.getPhoneNumber().getInternationalFormat(),otp );

           // return
        }
       return "failed";
    }
    public String verifyOTP(PhoneNumber phoneNumber, String otp){
        if(otpService.validateOTP(String.valueOf(phoneNumber), otp)){
            return "";
        }
        return "Invalid OTP";
    }

    public List<Client> getAllClients(){

        return clientRepository.findAll();
    }


}
