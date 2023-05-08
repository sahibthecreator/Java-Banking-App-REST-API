package com.bank.app.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="accounts")
public class Account {
    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "iban")
    private String iban;

    @Column(name = "balance")
    private float balance;

    @Column(name = "typeOfAccount")
    private AccountType typeOfAccount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "dateOfOpening")
    private String dateOfOpening;

    @Column(name = "absoluteLimit")
    private float absoluteLimit;

    @Column(name = "isActive")
    private boolean isActive;

}
