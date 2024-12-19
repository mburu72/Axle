package com.tumi.haul.service;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.BaseUser;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.User;
import com.tumi.haul.model.user.UserPrincipal;
import com.tumi.haul.repository.AdminRepository;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        BaseUser userEntity;

        if (isEmail(identifier)) {
            log.info("Checking by EMAIL");
            userEntity = findUserOrClientByEmail(new Email(identifier));
        } else {
            log.info("Checking by PHONE NUMBER");
            userEntity = findUserOrClientByPhoneNumber(new PhoneNumber(identifier));
        }

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with identifier: " + identifier);
        }

        // UserPrincipal will handle either a Client or User instance without casting
        return new UserPrincipal(userEntity, identifier);
    }

    private BaseUser findUserOrClientByEmail(Email email) {
        return userRepository.findByEmail(email)
                .map(user -> (BaseUser) user)
                .or(() -> clientRepository.findByEmail(email).map(client -> (BaseUser) client)
                        .or(() -> adminRepository.findByEmail(email).map(admin -> (BaseUser) admin))
                )

                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email.getValue()));
    }

    private BaseUser findUserOrClientByPhoneNumber(PhoneNumber phoneNumber) {
        log.info("Looking up by phone number: {}", phoneNumber);

        return userRepository.findByPhoneNumber(phoneNumber)
                .map(user -> (BaseUser) user)
                .or(() -> clientRepository.findByPhoneNumber(phoneNumber).map(client -> (BaseUser) client))
                .orElseThrow(() -> new UsernameNotFoundException("No user found with phone number: " + phoneNumber.getValue()));
    }

    private boolean isEmail(String identifier) {
        return identifier.contains("@");
    }
}
