package com.tumi.haul.repository;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Client;
import com.tumi.haul.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
   Optional<Client> findByPhoneNumber(PhoneNumber phoneNumber);
  Optional<Client> findByEmail(Email email);
    Optional<Client>findByEmailOrPhoneNumber(Email email, PhoneNumber phoneNumber);
}
