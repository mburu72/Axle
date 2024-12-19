package com.tumi.haul.controller;

import com.tumi.haul.model.refresh.RefreshTokenReq;
import com.tumi.haul.model.user.*;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.security.jwt.JwtResp;
import com.tumi.haul.security.jwt.RefreshTokenService;
import com.tumi.haul.service.UserService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private final RefreshTokenService refreshTokenService;

    public AuthController(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/driver-register")
    public ResponseEntity<?> registerHauler(@RequestBody RegistrationReq user){
        log.info("USER REGISTRATION: {}", user);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(user));
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }
    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody ClientRegisterReq user){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.createRegister(user));
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest user){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(userService.verify(user));
        }catch (Exception e){
            log.info("ERROR: {} ", e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }
    /*@PostMapping("/c-login")
    public ResponseEntity<?> cLogin(@RequestBody LoginReq user){
        try{
            BaseUser client = clientService.findUserByEmailOrPhoneNumber(user.getUsername());
            log.info("Here is the user: {}", client);
            if (!clientService.checkPassword(client, user.getPassword())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials!");
            }
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication auth = authManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return ResponseEntity.ok("Login successful");
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }
    }*/
    @PostMapping("/refreshToken")
    public JwtResp requestRefreshToken(@RequestBody RefreshTokenReq refreshTokenReq) {
        return refreshTokenService.findByToken(refreshTokenReq.getToken())
                .map(refreshTokenService::verifyRTExpiration)
                .map(refreshToken -> {
                    // Check if user is present
                    User user = refreshToken.getUser();
                    Client client = refreshToken.getClient();

                    // Determine which entity is present and generate the access token accordingly
                    String userId;
                    String accessToken;

                    if (user != null) {
                        userId = user.getId();
                        accessToken = jwtService.generateToken(user.getPhoneNumber().getValue(), userId, user.getRole());
                    } else if (client != null) {
                        userId = client.getId();
                        accessToken = jwtService.generateToken(client.getPhoneNumber().getValue(), userId, client.getRole());
                    } else {
                        throw new RuntimeException("No associated user or client found for the refresh token.");
                    }
                    return JwtResp.builder()
                            .jwtToken(accessToken)
                            .refreshToken(refreshTokenReq.getToken())
                            .tokenId(refreshTokenReq.getTokenId())
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is invalid!"));
    }

    @PostMapping("/logout/{tokenId}")
    public ResponseEntity<?> logout(@PathVariable String tokenId){
try{
    refreshTokenService.logout(tokenId);
    return ResponseEntity.status(HttpStatus.OK).body("");
}catch (Exception e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
}
    }

}
