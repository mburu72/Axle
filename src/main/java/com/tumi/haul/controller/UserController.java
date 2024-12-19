package com.tumi.haul.controller;

import com.tumi.haul.exception.UserNotFoundException;
import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.user.PasswordCreationReq;
import com.tumi.haul.model.user.UserResp;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.security.jwt.JwtResp;
import com.tumi.haul.service.UserService;
import com.tumi.haul.service.emailservice.EmailService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
   @Autowired
    private EmailService emailService;
    @Autowired
private OtpService otpService;
    @Autowired
    private JWTService jwtService;

/*
    @PostMapping("/request-otp")
    public String requestOTP(@RequestParam Email email){
        String otp = otpService
        log.info("Generated OTP is: {}", otp);
        return "OTP sent to " + email;
    }
*/
    @PostMapping("/{username}/request-otp")
    public String requestOTPToSms(@PathVariable String username) throws Exception {
        String otp = otpService.generatedOTPToDmd(username);
      // atSmsService.setUpAfricasTalking(username, otp);

        return "OTP sent to " + username;
    }
    @PostMapping("/{email}/{otp}/registration")
    public ResponseEntity<?>verifyRegistrationOTP(@PathVariable String otp, @PathVariable String email){
        try{
            JwtResp jwt = userService.verifyRegistrationOTP(otp, email);
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(jwt);
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
        }
    }
    @PostMapping("/{email}/{otp}/client")
    public ResponseEntity<?>verifyRegistrationOTPClient(@PathVariable String otp, @PathVariable String email){
        try{
            JwtResp jwt = userService.verifyRegClientOTP(otp, email);
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(jwt);
        }catch (Exception e){
            log.warn(e.toString());
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
        }
    }

    @PostMapping("/{username}/{otp}/verify-otp")
    public ResponseEntity<?>verifyOTP(@PathVariable String username, @PathVariable String otp){
      try{
  JwtResp jwt = userService.verifyOTP(username, otp);
          return ResponseEntity.status(HttpStatus.OK)
                  .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                  .body(jwt);
      }catch (Exception e){
          log.warn("ERROR! : {}", e.getLocalizedMessage());
          return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
      }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication auth){
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            return ResponseEntity.status(HttpStatus.OK).body( userService.getMe(userDetails.getUsername()));
  }

    @PostMapping("/{email}/{otp}/verify-user")
    public ResponseEntity<?> verifyCLientOtp(@PathVariable Email email,  @PathVariable String otp){

        try{
            JwtResp jwt = userService.verifyClientOtp(email, otp);
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                    .body(jwt);
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
        }
    }
    @PostMapping("/setup-password")
    public ResponseEntity<String> createPassword(@Validated @RequestBody PasswordCreationReq request) {
        try {
            userService.createPassword(request);
            return ResponseEntity.ok("Password created successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create password");
        }
    }
    @GetMapping("/users")
public List<UserResp> getAllClients(){
      return  userService.getAllUsers();
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
