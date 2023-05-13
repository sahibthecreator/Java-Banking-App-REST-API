package com.bank.app.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
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
    @JoinColumn(name = "userId", referencedColumnName = "uuid")
    @JsonIgnoreProperties("accounts")
    private User user;

    @Column(name = "dateOfOpening")
    private String dateOfOpening;

    @Column(name = "absoluteLimit")
    private float absoluteLimit;

    @Column(name = "isActive")
    private boolean isActive;

    public void setActive(boolean active) {
        isActive = active;
    }
}
