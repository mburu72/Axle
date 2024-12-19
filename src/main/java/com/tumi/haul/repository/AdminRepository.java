package com.tumi.haul.repository;

import com.tumi.haul.model.admin.model.Admin;
import com.tumi.haul.model.primitives.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    Optional<Admin> findByEmail(Email email);
}
