package com.tumi.haul.model.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private final BaseUser baseUser; // Changed to BaseUser to support both User and Client
    private final String identifier;
    private static final Logger logger = LoggerFactory.getLogger(UserPrincipal.class);

    public UserPrincipal(BaseUser baseUser, String identifier) {
        this.baseUser = baseUser;
        this.identifier = identifier;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Adjusted to use role from BaseUser (if roles are different between User and Client, customize this logic)
        return Collections.singleton(new SimpleGrantedAuthority(baseUser.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return baseUser.getPassword(); // Works for both User and Client
    }

    @Override
    public String getUsername() {
        return identifier; // Uses the login identifier (either email or phone number)
    }

    // Implement remaining methods for UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify as per your requirements
    }

    @Override
    public boolean isEnabled() {
        return true; // Modify as per your requirements
    }
}
