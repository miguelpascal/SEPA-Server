package com.ensicaen.sepa.models;

import com.ensicaen.sepa.enums.TypeCompte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;


// create jpa entity
@Entity
@Table(name="account")
// generate getter and setter with constructor having arguments or not using annotations given by lombok
@Data @NoArgsConstructor @AllArgsConstructor
public class Account {
    // define idCompte as the primary key of class Account
    // increment idCompte when adding an account
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long idAccount;
    @Column(unique=true)
    private  String iban;
    private String bic;
    private double amount;
    private Date dateCreation;
    private String currency;
    @Enumerated(EnumType.STRING)
    private TypeCompte type;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "customer",referencedColumnName = "email")
    private Customer user;
    //private Account beneficaire;
}