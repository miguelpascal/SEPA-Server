package com.ensicaen.sepa.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class IbanCreditDto implements Serializable {
    @NotNull(message = "The iban number of the order is missing")
    private  String ibanOrder;
    @NotNull(message = "The iban number of the beneficiary is missing")
    private String ibanBen;
    @NotNull(message = "The bank identifier number of the beneficiary is missing")
    private String bicBen;
    @NotNull(message = "the amount of the credit is missing")
    private double amount;
    @NotNull(message = "The reason of the transaction is missing")
    private String motif;
}
