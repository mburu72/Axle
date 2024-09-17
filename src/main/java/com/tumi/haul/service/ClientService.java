package com.tumi.haul.service;

import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.ClientResp;
import com.tumi.haul.repository.ClientRepository;
import com.tumi.haul.security.jwt.JWTService;
import com.tumi.haul.service.atsms.ATSmsService;
import com.tumi.haul.service.otpservice.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private ATSmsService atSmsService;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public String createUser(Client client) throws Exception {

          if(clientRepository.findByEmail(client.getEmail()).isPresent()){
              throw new IllegalArgumentException("User with this email already exists!");
          }
          if (clientRepository.findByPhoneNumber(client.getPhoneNumber()).isPresent()){
              throw new IllegalArgumentException("This number is already registered!");
          }
            client.setPassword(encoder.encode(client.getPassword()));
            clientRepository.save(client);
            return jwtService.generateToken(client.getPhoneNumber());
    }
    public String verify(Client client) throws Exception {
        Authentication auth =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(client.getPhoneNumber().getValue(), client.getPassword()));
        if (auth.isAuthenticated()){
               String otp=otpService.generatedOTPToDmd(client.getPhoneNumber().getValue());
            atSmsService.setUpAfricasTalking(client.getPhoneNumber().getInternationalFormat(), otp);

            return "success";
        }
       return "failed";
    }
    public String verifyOTP(String phoneNumber, String otp){
        try{
            if(!otpService.validateOTP(phoneNumber, otp)){
                log.info("no jtwtoken for you!");
            }else {
                log.info("here us your token!");
                String jwt = jwtService.generateToken(new PhoneNumber(phoneNumber));
//                ClientLoginResp clientLoginResp = new ClientLoginResp().getToken();
                log.info(jwt);
              return jwt;

            }
        }catch (Exception e){
            log.info(e.getLocalizedMessage());
        }
return null;
    }

    public List<ClientResp> getAllClients(){
        List<Client> clients = clientRepository.findAll();
        ClientResp clientResp ;
        List<ClientResp>clientResps=new ArrayList<>();
for (Client client:clients){
     clientResp=new ClientResp(
             client.getFirstName().getValue(), client.getLastName().getValue(), client.getRole().getAuthority()
     );

     clientResps.add(clientResp);
}
log.info(clientResps.toString());

     return  clientResps;
    }



}
