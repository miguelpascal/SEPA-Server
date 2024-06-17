package com.ensicaen.sepa.repositories;

import com.ensicaen.sepa.models.Account;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByUser_Email(@Email String email);
    Account findByIban(String iban);
    Account findByIbanAndBic(String iban, String bic);
 }
