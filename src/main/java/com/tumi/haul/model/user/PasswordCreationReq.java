package com.tumi.haul.model.user;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import lombok.Getter;

@Getter
public class PasswordCreationReq {
    private Email email;
    private String newPassword;

}
