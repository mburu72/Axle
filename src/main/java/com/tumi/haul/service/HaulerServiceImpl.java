package com.tumi.haul.service;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.Hauler;
import com.tumi.haul.model.user.HaulerPrincipal;
import com.tumi.haul.model.user.UserPrincipal;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.repository.HaulerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class HaulerServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private final HaulerRepository haulerRepository;

    public HaulerServiceImpl(HaulerRepository haulerRepository) {
        this.haulerRepository = haulerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Hauler hauler=haulerRepository.findByPhoneNumber(new PhoneNumber(username))
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        log.warn("Reaches here" + hauler.toString() + username);
        return new HaulerPrincipal(hauler);

    }
}
