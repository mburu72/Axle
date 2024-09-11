package com.tumi.haul.controller;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.service.ClientService;
import com.tumi.haul.service.atsms.ATSmsService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/users")
public class ClientController {
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    @Autowired
    private ClientService clientService;
    @Autowired
    private ATSmsService atSmsService;
  /*  @Autowired
    private EmailService emailService;*/
    @Autowired
private OtpService otpService;
    @Autowired
    private JWTService jwtService;
    @PostMapping("/register")
    public ResponseEntity<?>registerUser(@RequestBody Client client){
        try{
            clientService.createUser(client);
            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.ACCEPTED);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

    }
    /*
    @PostMapping("/request-otp")
    public String requestOTP(@RequestParam Email email){
        String otp = otpService.generatedOTP(email);
        log.info("Generated OTP is: {}", otp);
        return "OTP sent to " + email;
    }

    @PostMapping("/request-otpsms")
    public String requestOTPToSms(@RequestParam PhoneNumber phoneNumber) throws Exception {
        String otp = otpService.generatedOTPToDmd(phoneNumber.getInternationalFormat());
       aTsmsSender.sendOTP(phoneNumber.getValue(),otp);
        return "OTP sent to " + phoneNumber.getValue();
    }
*/
    @PostMapping("/verify-otp")
    public ResponseEntity<?>verifyOTP(@RequestParam PhoneNumber phoneNumber, @RequestParam String otp){
      try{
          clientService.verifyOTP(phoneNumber, otp);
          return ResponseEntity.status(200).body(jwtService.generateToken(phoneNumber));
      }catch (Exception e){
          return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
      }
    }
    @PostMapping("/login")
    public String login(@RequestBody Client client){
        try{
            clientService.verify(client);
         //  emailService.sendVerificationCode(client);
           //String otp = otpService.generatedOTP(client.getEmail());
            //String otp = otpService.generatedOTPToDmd(client.getPhoneNumber().getValue());
            atSmsService.setUpAfricasTalking(client);

            log.info("sent OTP to:${otp,}", client.getPhoneNumber());
                 return "We have sent OTP to:{}" + client.getPhoneNumber().getValue();

        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return "Failed";
        }

    }
  

/*
    @PostMapping("/generate-token")
    public String createAuthenticationToken(@RequestBody Client authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getPhoneNumber(), "")
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = clientService
                .loadUserByUsername(String.valueOf(authenticationRequest.getPhoneNumber()));
        return jwtTokenUtil.generateToken(String.valueOf(userDetails));
    }

    @DeleteMapping
    public void deleteUser(Long id){
        if (clientService.getUserById(id).isEmpty()){
            throw new IllegalArgumentException("User does not exist!");
        }
        clientService.deleteUser(id);
    }*/
}
