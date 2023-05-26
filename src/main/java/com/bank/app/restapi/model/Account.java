package com.bank.app.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name="accounts")
public class Account {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "iban")
    private String iban;

    @Column(name = "balance")
    private float balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeOfAccount")
    private AccountType typeOfAccount;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    @JsonIgnoreProperties("accounts")
    private User user;

    @Column(name = "dateOfOpening")
    private LocalDate dateOfOpening;

    @Column(name = "absoluteLimit")
    private float absoluteLimit;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "fromAccount")
    @JsonIgnore
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "toAccount")
    @JsonIgnore
    private List<Transaction> receivedTransactions;

}
