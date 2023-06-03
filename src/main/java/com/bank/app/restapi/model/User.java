package com.bank.app.restapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Column(name = "role")
    private UserType role;

    @Column(name = "dayLimit")
    private float dayLimit;

    @Column(name = "transactionLimit")
    private float transactionLimit;

    @Column(name = "active")
    private boolean active;

}
