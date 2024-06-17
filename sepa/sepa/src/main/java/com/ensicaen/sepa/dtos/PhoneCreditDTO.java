package com.ensicaen.sepa.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PhoneCreditDTO {
    @NotNull(message = "The phone number of the order is missing")
    private String ibanOrder;
    @NotNull(message = "The phone number of the beneficiary is missing")
    private String benefPhoneNumber ;
    @NotNull(message = "the amount of the credit is missing")
    private double amount;
    @NotNull(message = "The reason of the transaction is missing")
    private String motif;
}
