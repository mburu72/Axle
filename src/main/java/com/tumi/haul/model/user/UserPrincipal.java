package com.tumi.haul.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements org.springframework.security.core.userdetails.UserDetails {
private final Client client;
public UserPrincipal(Client client){
    this.client=client;
}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getPhoneNumber().getValue();
    }
}
