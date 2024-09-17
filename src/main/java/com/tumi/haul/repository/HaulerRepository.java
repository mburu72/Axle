package com.tumi.haul.repository;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.user.Hauler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HaulerRepository extends JpaRepository<Hauler, Long> {
    Boolean existsByEmail(Email email);
    Optional<Hauler> findByPhoneNumber(PhoneNumber phoneNumber);
    Optional<Hauler> findByEmail(Email email);
    //Optional<?>findByRole(Client client);

}
