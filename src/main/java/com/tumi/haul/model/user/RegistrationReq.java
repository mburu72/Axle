package com.tumi.haul.model.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RegistrationReq {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String idNumber;
    private String role;
    private MultipartFile idCardFront;
    private MultipartFile idCardBack;
    private MultipartFile licenseFront;
    private MultipartFile licenseBack;
    private MultipartFile passportPhoto;
}
