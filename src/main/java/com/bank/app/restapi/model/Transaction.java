package com.bank.app.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "fromIban", referencedColumnName = "iban")
    private Account fromIban;

    @ManyToOne
    @JoinColumn(name = "toIban", referencedColumnName = "iban")
    private Account toIban;

    @Column(name = "amount")
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeOfTransaction")
    private TransactionType typeOfTransaction;

    @Column(name = "dateOfExecution")
    private LocalDateTime dateOfExecution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("transactions")
    private User performingUser;

    @Column(name = "description")
    private String description;
}
