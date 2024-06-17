package com.ensicaen.sepa.repositories;


import com.ensicaen.sepa.models.CreditOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditOperationRepository extends JpaRepository<CreditOperation,Long> {
    List<CreditOperation> findAllByIbanOrder(String iban);
}
