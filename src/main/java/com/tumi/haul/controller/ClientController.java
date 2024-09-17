package com.tumi.haul.controller;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.ClientResp;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.service.ClientService;
import com.tumi.haul.service.atsms.ATSmsService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            return ResponseEntity.status(HttpStatus.OK).body(clientService.createUser(client));
        }catch (Exception e){
         log.warn(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }
    /*
    @PostMapping("/request-otp")
    public String requestOTP(@RequestParam Email email){
        String otp = otpService.generatedOTP(email);
        log.info("Generated OTP is: {}", otp);
        return "OTP sent to " + email;
    }
*/
    @PostMapping("/{phoneNumber}/request-otp")
    public String requestOTPToSms(@PathVariable PhoneNumber phoneNumber) throws Exception {
        String otp = otpService.generatedOTPToDmd(phoneNumber.getValue());
       //aTsmsSender.sendOTP(phoneNumber.getValue(),otp);
        return "OTP sent to " + phoneNumber.getValue();
    }

    @PostMapping("/{phoneNumber}/{otp}/verify-otp")
    public ResponseEntity<?>verifyOTP(@PathVariable PhoneNumber phoneNumber, @PathVariable String otp){
      try{
  String jwt = clientService.verifyOTP(phoneNumber.getValue(), otp);
          return ResponseEntity.status(HttpStatus.OK)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .body(jwt);
      }catch (Exception e){
          return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
      }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Client client){
        try{

         //  emailService.sendVerificationCode(client);
           //String otp = otpService.generatedOTP(client.getEmail());
            log.info(clientService.verify(client));
                 return ResponseEntity.status(HttpStatus.OK).body(clientService.verify(client));

        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }

    @GetMapping("/clients")
public List<ClientResp> getAllClients(){
      return  clientService.getAllClients();
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
