package com.tumi.haul.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    private String username; // can be either email or phone number
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isEmailLogin() {
        return username != null && username.contains("@");
    }

    public boolean isValid() {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }
}
