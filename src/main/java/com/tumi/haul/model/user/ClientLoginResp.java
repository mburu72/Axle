package com.tumi.haul.model.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientLoginResp {

    private String token;
    private long expiresIn;


}
