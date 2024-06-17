package com.ensicaen.sepa.models;

import com.ensicaen.sepa.enums.CreditMode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "operation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditOperation {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long idOperation;
    @JoinColumn(name = "compte", referencedColumnName = "iban")
    private  String ibanOrder;
    @Enumerated(EnumType.STRING)
    private CreditMode mode;
    @NotNull
    private  String destinataire;
    @NotNull(message = "username shouldn't be null")
    private String lastNameBenef;
    private String firstNameBenef;
    @JoinColumn(name = "customer", referencedColumnName = "phoneNumber")
    @NotNull
    private double Amount;
    private String motif;
    private Date transactionDate;

}
