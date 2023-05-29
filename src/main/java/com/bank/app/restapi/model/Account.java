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
import java.util.Objects;
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

    //For comparison if entered account is the same as BANK's bank account, in case of DEPOSIT or WITHDRAWAL - Manol
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account other = (Account) obj;
        return Objects.equals(id, other.id) && Objects.equals(iban, other.iban);
    }
}
