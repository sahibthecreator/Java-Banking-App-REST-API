package com.bank.app.restapi.repository;

import com.bank.app.restapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}