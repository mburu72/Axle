package com.tumi.haul.model.user;

import lombok.Data;

@Data
public class ClientResp {
    private Client client;
    private String name;
    private String email;
    private String phoneNumber;
    public ClientResp(String name, String email, String phoneNumber){
        this.name= client.getName().getValue();
        this.email=client.getEmail().getValue();
        this.phoneNumber=client.getPhoneNumber().getValue();
    }
}
