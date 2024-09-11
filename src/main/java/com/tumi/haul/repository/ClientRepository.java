package com.tumi.haul.repository;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.primitives.Role;
import com.tumi.haul.model.user.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Boolean existsByEmail(Email email);
    Optional<Client>findByPhoneNumber(PhoneNumber phoneNumber);
    Optional<Client> findByEmail(Email email);
    Optional<?>findByRole(Client client);

}