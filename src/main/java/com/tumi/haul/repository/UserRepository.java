package com.tumi.haul.repository;

import com.tumi.haul.model.primitives.Email;
import com.tumi.haul.model.primitives.PhoneNumber;
import com.tumi.haul.model.primitives.UserId;
import com.tumi.haul.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User>findByPhoneNumber(PhoneNumber phoneNumber);
    Optional<User> findByEmail(Email email);
    Optional<?>findByRole(User User);


}