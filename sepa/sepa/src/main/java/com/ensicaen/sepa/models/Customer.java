package com.ensicaen.sepa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.util.Date;
// create jpa entity
@Entity
@Table(name="customer")
// generate getter and setter with constructor having arguments or not using annotations given by lombok
@Data @NoArgsConstructor @AllArgsConstructor
public class Customer {
    // define idCustomer as the primary key of class Customer
    // increment idCustomer when adding a user
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long idCustomer;
    @Column(unique=true)
    @NotNull
    @Email(message = "invalid email address")
    private String email;
    protected String password;
    @NotNull(message = "username shouldn't be null")
    private String lastName;
    private String firstName;
    @NotNull
    @Column(unique=true)
    @NotNull
    private String phoneNumber ;

    private Date dateCreation;


}