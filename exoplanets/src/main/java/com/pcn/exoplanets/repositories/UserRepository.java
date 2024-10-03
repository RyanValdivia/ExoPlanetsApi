package com.pcn.exoplanets.repositories;

import com.pcn.exoplanets.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository <User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
