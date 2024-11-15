package com.example.acmeplex.usersystem.repository;

import com.example.acmeplex.usersystem.model.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {
    
    Optional<RegisteredUser> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // more custom query methods.. later
}
