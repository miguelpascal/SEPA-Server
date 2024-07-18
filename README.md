# Sepa Application server

This Server is used to simulate SEPA Credit Transfer Inst. The framework used for the purpose is SpringBoot with the Intellij IDE

## Configuration fonctionnelle
Une classe de configuration fonctionnelle du virement est défini avec un fichier de configuration associé se trouvant dans le répertoire 

### sepa/src/main/resources/sepa.properties ###

La définition de la classe est la suivante:
```java
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

```
## Installation

A docker image will be given to deploy this server in every environment. This image will be given through our Docher Hub [docker image](https://hub.docker.com/r/pascalmiguel/sepav1/tags). To run the image in your environment, use the following command:
```bash
docker run -d -p 3000:8080 --name=sepa sepa:0.0.1
```



## Usage
Une Api doc générée avec [Swager](https://swagger.io/) est fournie avec le server et accessible à travers le lien [Api Docs](http://127.0.0.1/swagger-ui/index.html) après le run de l'image docker. Dans cette classe, nous pouvons définir tous les potentiels utilisateurs et leur compte
```java
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

```

Le controlleur maître du projet est décrit de la façon suivante:


```java
public class AccountController {

    private final AccountService accountService;

    // Sign in and get the all account information of the logger user
    @PostMapping(path = "/singIn")
    public CompteDto signIn(@RequestBody String email) throws SepaApplicationException {
        return accountService.getAccountByEmail(email);
    }

    // Go Back to the home page after credit
    @GetMapping(path = "user/home")
    public CompteDto Home(@RequestParam Long userID) throws SepaApplicationException {
        return accountService.getAccountByUserId(userID);
    }

    // SEPA credit Transfer Inst with Iban
    @PostMapping(path = "iban/credit")
    public void creditAccount(@RequestBody @NotNull IbanCreditDto ibanCreditDto) throws SepaApplicationException {
        accountService.creditAccountByIban(ibanCreditDto);
    }

    // SEPA credit Transfer Inst with Phone Number
    @PostMapping(path = "/phone/credit")
    public void creditAccountWithNumber(@RequestBody @NotNull PhoneCreditDTO phoneCreditDTO) throws SepaApplicationException {
        accountService.creditAccountByPhoneNumber(phoneCreditDTO);
    }

    // Get history of SCT Inst Operation (Using Phone and Iban)  
    @GetMapping(path = "/credit/history/{userID}")
    public List<CreditOperation> creditHistory(@PathVariable Long userID) throws SepaApplicationException {
        return accountService.creditHistory(userID);
    }
}
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
