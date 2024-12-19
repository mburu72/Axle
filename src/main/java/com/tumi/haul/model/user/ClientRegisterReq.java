package com.tumi.haul.model.user;

import lombok.Getter;

@Getter
public class ClientRegisterReq {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
}
