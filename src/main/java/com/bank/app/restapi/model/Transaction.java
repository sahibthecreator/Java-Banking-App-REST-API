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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fromIban", referencedColumnName = "iban", nullable = false)
    @JsonIgnoreProperties("sentTransactions")
    private Account fromAccount;

    @ManyToOne
    @JoinColumn(name = "toIban", referencedColumnName = "iban", nullable = false)
    @JsonIgnoreProperties("receivedTransactions")
    private Account toAccount;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeOfTransaction", nullable = false)
    private TransactionType typeOfTransaction;

    @Column(name = "dateOfExecution", nullable = false)
    private LocalDateTime dateOfExecution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId") // userId for consistency 
    @JsonIgnoreProperties("transactions")
    private User performingUser;

    @Column(name = "description")
    private String description;
}
