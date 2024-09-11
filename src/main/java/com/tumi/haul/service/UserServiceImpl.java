package com.tumi.haul.service;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.UserPrincipal;
import com.tumi.haul.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ClientRepository clientRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Client client=clientRepository.findByPhoneNumber(new PhoneNumber(username))
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        log.warn("Reaches here" + client.toString() + username);
        return new UserPrincipal(client);

    }


}
