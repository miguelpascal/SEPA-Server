package com.ensicaen.sepa.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// generate getter and setter with constructor having arguments or not using annotations given by lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteDto implements Serializable {
    private  Long customerID;
    private  String iban;
    private String bic;
    private double balance;
    private String currency;
    private String lastName;
    private String firstName;

}


