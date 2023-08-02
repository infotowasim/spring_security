package com.happiestminds.springsecurity.repositories;

import com.happiestminds.springsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmailOrUsername(String username, String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
