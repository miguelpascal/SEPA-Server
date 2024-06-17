package com.ensicaen.sepa.repositories;

import com.ensicaen.sepa.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long>{
    Customer findByEmail(String email);
    Customer findByPhoneNumber(String phoneNumber);

    Customer findByIdCustomer(Long id);
}