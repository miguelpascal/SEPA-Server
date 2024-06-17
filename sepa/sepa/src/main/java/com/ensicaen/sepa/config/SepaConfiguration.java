package com.ensicaen.sepa.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SepaConfiguration {
    @Value("${sct.thresholdAmount}")
    private double thresholdAmount;

    @Value("${sct.currency}")
    private String currency;

}
