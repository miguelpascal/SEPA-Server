package com.ensicaen.sepa;

import com.ensicaen.sepa.models.Account;
import com.ensicaen.sepa.enums.TypeCompte;
import com.ensicaen.sepa.repositories.AccountRepository;
import com.ensicaen.sepa.models.Customer;
import com.ensicaen.sepa.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.Date;

@SpringBootApplication
@PropertySource("classpath:sepa.properties")
public class SepaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SepaApplication.class, args);
	}
	@Bean
	CommandLineRunner initDatabase(AccountRepository accountRepository, CustomerRepository customerRepository){

		return args -> {
			if (customerRepository.findByEmail("miguel@gmail.com")==null && customerRepository.findByEmail("pamela@gmail.com")==null && customerRepository.findByEmail("emmanuel@gmail.com")==null && customerRepository.findByEmail("bertin@gmail.com")==null) {
				customerRepository.save(new Customer(null, "miguel@gmail.com", "1234", "Kamdem", "Pascal Miguel", "+33681456064", new Date()));
				customerRepository.save(new Customer(null, "pamela@gmail.com", "1234", "Monthe", "Juimo Pamela", "+33674978013", new Date()));
				customerRepository.save(new Customer(null, "bertin@gmail.com", "1234", "LOUBAM", "Bertin Beriot", "+33628821049", new Date()));
				customerRepository.save(new Customer(null, "emmanuel@gmail.com", "1234", "ATANGANA ATANGANA", "Jean Emmanuel", "+33612489039", new Date()));
				Customer pamela = customerRepository.findByEmail("pamela@gmail.com");
				Customer miguel = customerRepository.findByEmail("miguel@gmail.com");
				Customer bertin = customerRepository.findByEmail("bertin@gmail.com");
				Customer emmanuel = customerRepository.findByEmail("emmanuel@gmail.com");
				accountRepository.save(new Account(null, "FR76 3000 1141 0000 0011 0023 136", "BNPAFRPPXXX", 20000, new Date(), "Euro", TypeCompte.CHEQUE, pamela));
				accountRepository.save(new Account(null, "FR76 3000 1140 0000 0010 0023 135", "BNPAFRPPXXX", 20000, new Date(), "Euro", TypeCompte.CHEQUE, miguel));
				accountRepository.save(new Account(null, "FR76 3000 1140 0000 0012 0023 134", "BNPAFRPPXXX", 20000, new Date(), "Euro", TypeCompte.CHEQUE, bertin));
				accountRepository.save(new Account(null, "FR76 3000 1140 0000 0013 0023 133", "BNPAFRPPXXX", 20000, new Date(), "Euro", TypeCompte.CHEQUE, emmanuel));
			}
		};
	}
}
