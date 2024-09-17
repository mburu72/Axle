package com.tumi.haul.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class HaulerPrincipal implements org.springframework.security.core.userdetails.UserDetails {
    private final Hauler hauler;
    public HaulerPrincipal(Hauler hauler){
        this.hauler=hauler;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return hauler.getPassword();
    }

    @Override
    public String getUsername() {
        return hauler.getPhoneNumber().getValue();
    }
}
