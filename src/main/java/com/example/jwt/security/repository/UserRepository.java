package com.example.jwt.security.repository;

import com.example.jwt.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);
}
