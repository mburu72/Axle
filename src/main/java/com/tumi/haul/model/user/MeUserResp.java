package com.tumi.haul.model.user;
public record MeUserResp (
        String id,
        String firstName,
        String lastName,
        String phoneNumber,
        String role,
        String password
){

}
