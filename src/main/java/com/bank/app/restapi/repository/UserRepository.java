package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    @Transactional
    public void deleteById(UUID id);
}