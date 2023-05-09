package com.bank.app.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name = "uuid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "bsn")
    private String bsn;

    @Column(name = "dateOfBirth")
    private LocalDate dateOfBirth;

    @Column(name = "typeOfUser")
    private UserType typeOfUser;

    @Column(name = "dayLimit")
    private float dayLimit;

    @Column(name = "dayLitransactionLimitmit")
    private float transactionLimit;

    @OneToMany(mappedBy = "users")
    @JsonIgnoreProperties("users")
    private Set<Account> accounts = new HashSet<>();
}
