package com.tumi.haul.service;

import com.tumi.haul.exception.InvalidCredentialsException;
import com.tumi.haul.exception.UserNotFoundException;
import com.tumi.haul.model.admin.model.Admin;
import com.tumi.haul.model.enums.Roles;
import com.tumi.haul.model.primitives.*;
import com.tumi.haul.model.refresh.RefreshToken;
import com.tumi.haul.model.user.*;
import com.tumi.haul.repository.AdminRepository;
import com.tumi.haul.repository.UserRepository;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.security.jwt.JwtResp;
import com.tumi.haul.security.jwt.RefreshTokenService;
import com.tumi.haul.service.emailservice.EmailService;
import com.tumi.haul.service.otpservice.OtpService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final OtpService otpService;
    private final JWTService jwtService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authManager;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final Map<String, RegistrationReq> userCache = new HashMap<>();
    private final Map<String, ClientRegisterReq> clientCache = new HashMap<>();
    @Autowired
    public UserService(AdminRepository adminRepository, UserRepository userRepository, ClientRepository clientRepository, OtpService otpService, JWTService jwtService, EmailService emailService, RefreshTokenService refreshTokenService, AuthenticationManager authManager) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.otpService = otpService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.authManager = authManager;
    }
    public String createUser(RegistrationReq user) throws Exception {

        if (userRepository.findByEmail(new Email(user.getEmail())).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists!");
        }
        if (userRepository.findByPhoneNumber(new PhoneNumber(user.getPhoneNumber())).isPresent()) {
            throw new IllegalArgumentException("This number is already registered!");
        }
        String otp = otpService.generatedOTPToDmd(user.getEmail());
        emailService.sendVerificationCode(user.getEmail(), otp);
        userCache.put(user.getEmail(), user);
        //  atSmsService.setUpAfricasTalking(user.getPhoneNumber().getInternationalFormat(), otp);

        return "Success";
    }

    public JwtResp verifyRegistrationOTP(String otp, String email) {
        if(otpService.validateOTP(email, otp)){
            RegistrationReq newUser = userCache.get(email);
            User user = new User();
            user.setFirstName(new Name(newUser.getFirstName()));
            user.setLastName(new Name(newUser.getLastName()));
            user.setRole(Roles.HAULER);
            user.setPhoneNumber(new PhoneNumber(newUser.getPhoneNumber()));
            user.setEmail(new Email(newUser.getEmail()));
            user.setPassword(encoder.encode(newUser.getPassword()));
           user.setIdNumber(new IDNumber(newUser.getIdNumber()));
            userRepository.save(user);
            String phoneNumber = user.getPhoneNumber().getValue();
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(phoneNumber);
            String jwt = jwtService.generateToken(phoneNumber, user.getId(), user.getRole());
            return new JwtResp(jwt, refreshToken.getToken(), refreshToken.getId());
        }
        return null;

    }

    public String verify(LoginRequest loginRequest) throws MessagingException, UnsupportedEncodingException, InvalidCredentialsException {
try{
    Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
    );
    log.info("Authentication success: {}", auth);

    if (auth.isAuthenticated()) {
        String otp = otpService.generatedOTPToDmd(loginRequest.getUsername());
        if (loginRequest.isEmailLogin()) {
            User user = userRepository.findByEmail(new Email(loginRequest.getUsername()))
                    .orElse(null);

            if (user != null) {
                emailService.sendVerificationCode(user.getEmail().getValue(), otp);
                return "Verification OTP sent to user";
            }
            Client client = clientRepository.findByEmail(new Email(loginRequest.getUsername()))
                    .orElse(null);
           if (client != null){
               emailService.sendVerificationCode(client.getEmail().getValue(), otp);
               return "Verification OTP sent to client";
           }
            Admin admin = adminRepository.findByEmail(new Email(loginRequest.getUsername()))
                    .orElseThrow(()->new UserNotFoundException("User not found!"));
            return "Verification OTP sent to admin";
        } else {
            User user = userRepository.findByPhoneNumber(new PhoneNumber(loginRequest.getUsername()))
                    .orElse(null);
            if (user != null) {
                log.info(otp);
               emailService.sendVerificationCode(user.getEmail().getValue(), otp);
                return "Verification OTP sent to user";
            }

            Client client = clientRepository.findByPhoneNumber(new PhoneNumber(loginRequest.getUsername()))
                    .orElseThrow(()-> new UserNotFoundException("User not found!"));
            emailService.sendVerificationCode(client.getEmail().getValue(), otp);
            return "Verification OTP sent to client";
        }
    }
    else {
        throw new InvalidCredentialsException("Invalid Credentials, please try again");
    }
}catch (BadCredentialsException BCE){
    throw new UserNotFoundException("Invalid credentials.Please try again");
}

    }

    public JwtResp verifyOTP(String username, String otp) throws InvalidCredentialsException {
        User user = username.contains("@") ?
                userRepository.findByEmail(new Email(username)).orElse(null):
                userRepository.findByPhoneNumber(new PhoneNumber(username)).orElse(null);

           Admin admin = adminRepository.findByEmail(new Email(username))
                   .orElse(null);

        if (admin == null) {
            Client client = username.contains("@") ?
                    clientRepository.findByEmail(new Email(username)).orElseThrow(() -> new UsernameNotFoundException("User not found!")) :
                    clientRepository.findByPhoneNumber(new PhoneNumber(username)).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

            if (otpService.validateOTP(username, otp)) {
                RefreshToken refreshToken = refreshTokenService.generateRefreshToken(client.getPhoneNumber().getValue());
                String jwt = jwtService.generateToken(username, client.getId(), client.getRole());
                return new JwtResp(jwt, refreshToken.getToken(), refreshToken.getId());
            }
            throw new IllegalArgumentException("Invalid OTP!");
        }

        if (otpService.validateOTP(username, otp)) {
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(user.getPhoneNumber().getValue());
            String jwt = jwtService.generateToken(username, user.getId(), user.getRole());
            return new JwtResp(jwt, refreshToken.getToken(), refreshToken.getId());
        }
        throw new IllegalArgumentException("Invalid OTP!");
    }

    public MeUserResp getMe(String username) {
        try{
            User user = username.contains("@") ?
                    userRepository.findByEmail(new Email(username)).orElse(null):
                    userRepository.findByPhoneNumber(new PhoneNumber(username)).orElse(null);
            if (user != null) {
                return new MeUserResp(user.getId(), user.getFirstName().getValue(), user.getLastName().getValue(), user.getPhoneNumber().getValue(), user.getRole().getAuthority(), user.getPassword());
            }else{
                Client client = username.contains("@") ?
                        clientRepository.findByEmail(new Email(username)).orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + username)) :
                        clientRepository.findByPhoneNumber(new PhoneNumber(username)).orElseThrow(() -> new UsernameNotFoundException("Client not found with phone number: " + username));
                if(client != null){
                    return new MeUserResp(client.getId(),
                            client.getFirstName().getValue(), client.getLastName().getValue(),
                            client.getPhoneNumber().getValue(), client.getRole().getAuthority(), client.getPassword());
                }else {
                    return null;
                }
            }

        }catch (Exception e){
            return null;
        }
      }

    public List<UserResp> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserResp> users = new ArrayList<>();
        for (User user : userList) {
            users.add(new UserResp(user.getId(), user.getFirstName().getValue(), user.getLastName().getValue(), user.getPhoneNumber().getValue(), user.getRole().getAuthority()));
        }
        return users;
    }

    public JwtResp createClient(Client client) throws Exception {
        client.setPassword(null);
        String otp = otpService.generatedOTPToDmd(client.getEmail().getValue());
        clientRepository.save(client);
      emailService.sendVerificationCode(client.getEmail().getValue(), otp);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(client.getPhoneNumber().getValue());
        String jwt = jwtService.generateToken(client.getPhoneNumber().getValue(), client.getId(), client.getRole());

        return new JwtResp(jwt, refreshToken.getToken(), refreshToken.getId());

    }
    public JwtResp verifyClientOtp(Email email, String otp) {
        Client client = clientRepository.findByEmail(email)
                .orElse(null);

        if (client != null) {
            if(otpService.validateOTP(email.getValue(), otp)){
                client.setVerified(true);
                RefreshToken refreshToken = refreshTokenService.generateRefreshToken(client.getPhoneNumber().getValue());
                String jwt = jwtService.generateToken(client.getEmail().getValue(), client.getId(), client.getRole());
                return new JwtResp(jwt, refreshToken.getToken(), refreshToken.getId());
            }else {
                return null;
            }

        } else {
            return null;
        }

    }
        public void createPassword(PasswordCreationReq request) throws UserNotFoundException {
           Client client = clientRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            client.setPassword(encoder.encode(request.getNewPassword()));
            clientRepository.save(client);
        }
    public String createRegister(ClientRegisterReq client) throws Exception {

        if (clientRepository.findByEmail(new Email(client.getEmail())).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists!");
        }
        if (clientRepository.findByPhoneNumber(new PhoneNumber(client.getPhoneNumber())).isPresent()) {
            throw new IllegalArgumentException("This number is already registered!");
        }
        String otp = otpService.generatedOTPToDmd(client.getEmail());
        emailService.sendVerificationCode(client.getEmail(), otp);
        //  atSmsService.setUpAfricasTalking(user.getPhoneNumber().getInternationalFormat(), otp);
       clientCache.put(client.getEmail(), client);
       log.warn("WELCOME: {}", client.getFirstName() + client.getLastName());
        return "Success";
    }

    public JwtResp verifyRegClientOTP(String otp, String email) {
        if(otpService.validateOTP(email, otp)){
           ClientRegisterReq newClient = clientCache.get(email);
           Client client = new Client();
           client.setFirstName(new Name(newClient.getFirstName()));
           client.setLastName(new Name(newClient.getLastName()));
           client.setPassword(encoder.encode(newClient.getPassword()));
           client.setPhoneNumber(new PhoneNumber(newClient.getPhoneNumber()));
           client.setEmail(new Email(newClient.getEmail()));
           client.setRole(Roles.CLIENT);
           client.setVerified(true);
            clientRepository.save(client);
            String phoneNumber = client.getPhoneNumber().getValue();
            RefreshToken refreshToken = refreshTokenService.generateRefreshToken(phoneNumber);
            String jwt = jwtService.generateToken(phoneNumber, client.getId(), client.getRole());
            return new JwtResp(jwt, refreshToken.getToken(), refreshToken.getId());
        }
        return null;

    }
    }


