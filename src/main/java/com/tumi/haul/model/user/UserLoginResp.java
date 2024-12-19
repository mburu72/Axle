package com.tumi.haul.model.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginResp {

    private String token;
    private long expiresIn;


}
