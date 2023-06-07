package com.bank.app.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accountRequests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "accountType")
    private AccountType accountType;

    //Why not ENUM?
    @Column(name = "status")
    private String status;
}