package com.ensicaen.sepa.services;

import com.ensicaen.sepa.config.SepaConfiguration;
import com.ensicaen.sepa.dtos.CompteDto;
import com.ensicaen.sepa.dtos.IbanCreditDto;
import com.ensicaen.sepa.dtos.PhoneCreditDTO;
import com.ensicaen.sepa.enums.CreditMode;
import com.ensicaen.sepa.exceptions.SepaApplicationException;
import com.ensicaen.sepa.models.Account;
import com.ensicaen.sepa.models.CreditOperation;
import com.ensicaen.sepa.models.Customer;
import com.ensicaen.sepa.repositories.AccountRepository;
import com.ensicaen.sepa.repositories.CreditOperationRepository;
import com.ensicaen.sepa.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final CreditOperationRepository creditOperationRepository;
    private final SepaConfiguration sepaConfiguration;

    // get the list of the accounts in the database
    public List<Account> getListAccount(){
        System.out.println("Liste des comptes");
        return accountRepository.findAll();
    }

    public Account getAccountByIban(String iban) throws SepaApplicationException
    {
        Account account = accountRepository.findByIban(iban);
        if (account != null)
            return account;
        else throw new SepaApplicationException("There is no account linked to this iban");
    }
    public Account getAccountByIbanAndBic(String iban, String bic) throws SepaApplicationException {
        Account account = accountRepository.findByIbanAndBic(iban,bic);
        if (account != null)
            return account;
        else throw new SepaApplicationException("Iban or BIC does not Exist");
    }

    public CompteDto getAccountByUserId(Long id) throws SepaApplicationException {
        String email;
        try {
            Customer customer = customerRepository.findByIdCustomer(id);
            email = customer.getEmail();
        } catch (Exception e) {
            throw new SepaApplicationException("User with this id does not exist");
        }
        return getAccountByEmail(email);
    }

    public Account getAccountByPhone(String phoneNumber) throws SepaApplicationException {
        String email;
        try {
            email = customerRepository.findByPhoneNumber(phoneNumber).getEmail();
        } catch (Exception e) {
            throw new SepaApplicationException("There is no account linked to this phone number");
        }
        Account account = accountRepository.findByUser_Email(email);
        if (account != null)
            return account;
        else throw new SepaApplicationException("There is no account linked to this phone number");
    }

    // get an account by using email of the user
    public CompteDto getAccountByEmail(String email ) throws SepaApplicationException {
        System.out.println("Connexion");
        Account account = accountRepository.findByUser_Email(email);
        System.out.println("Connexion Terminée");
        if (account != null)
            return new CompteDto(account.getUser().getIdCustomer(),account.getIban(), account.getBic(), account.getAmount(), account.getCurrency(), account.getUser().getLastName(), account.getUser().getFirstName());
        else throw new SepaApplicationException("There is no account linked to this email: " + email);

    }

    // modify an account according to his id
    public void updateAccount(Long id, Account account) throws SepaApplicationException {
        try {
            account.setIdAccount(id);
            accountRepository.save(account);
        } catch (Exception e){
            throw new SepaApplicationException("Failed to update account");
        }
    }

    private void saveCreditOperation(CreditOperation creditOperation) throws SepaApplicationException {
        try{
            creditOperationRepository.save(creditOperation);
        } catch (Exception e){
            throw new SepaApplicationException("Failed to save the transaction");
        }

    }
    private void SetAccountAmount(Account order, Account beneficiary, double amount) throws SepaApplicationException {
        try{
            order.setAmount(order.getAmount()- amount);
            beneficiary.setAmount(beneficiary.getAmount() + amount);
            updateAccount(order.getIdAccount(),order);
            updateAccount(beneficiary.getIdAccount(), beneficiary);
        }  catch (Exception e){
            throw new SepaApplicationException("Transaction Credit Failed");
        }
    }

    public void creditAccountByIban(IbanCreditDto ibanCreditDto) throws SepaApplicationException {

        Account order = getAccountByIban(ibanCreditDto.getIbanOrder());
        if (order.getAmount()> ibanCreditDto.getAmount()) {
            if (ibanCreditDto.getAmount()>0){
                if (ibanCreditDto.getAmount()< sepaConfiguration.getThresholdAmount()) {
                    Account beneficiary = getAccountByIbanAndBic(ibanCreditDto.getIbanBen(), ibanCreditDto.getBicBen());
                    if (!Objects.equals(ibanCreditDto.getIbanBen(), ibanCreditDto.getIbanOrder())) {
                        SetAccountAmount(order, beneficiary, ibanCreditDto.getAmount());
                        try {
                            CreditOperation creditOperation = new CreditOperation(null, ibanCreditDto.getIbanOrder(), CreditMode.IBAN, ibanCreditDto.getIbanBen(), beneficiary.getUser().getLastName(), beneficiary.getUser().getFirstName(), ibanCreditDto.getAmount(), ibanCreditDto.getMotif(), new Date());
                            saveCreditOperation(creditOperation);
                        } catch (Exception e) {
                            throw new SepaApplicationException("Failed to save the credit Transaction");
                        }
                    } else throw new SepaApplicationException("You can't credit yourself");
                } else throw new SepaApplicationException("The amount of the transaction can't be upper than " + sepaConfiguration.getThresholdAmount()+ "  " + sepaConfiguration.getCurrency());
            }else throw new SepaApplicationException("The amount of the transaction can't be negative, equal to zero");

        } else throw new SepaApplicationException("The balance of your Account is insufficient to do the transaction ");


    }

    public void creditAccountByPhoneNumber(PhoneCreditDTO phoneCreditDTO) throws SepaApplicationException {

            Account order = getAccountByIban(phoneCreditDTO.getIbanOrder());
            if (order.getAmount()> phoneCreditDTO.getAmount()) {
                if (phoneCreditDTO.getAmount()>0){
                    if (phoneCreditDTO.getAmount()< sepaConfiguration.getThresholdAmount()) {
                        Account beneficiary = getAccountByPhone(phoneCreditDTO.getBenefPhoneNumber());
                        if (!Objects.equals(beneficiary.getIban(), phoneCreditDTO.getIbanOrder())){
                            SetAccountAmount(order, beneficiary, phoneCreditDTO.getAmount());
                            try{
                                CreditOperation creditOperation = new CreditOperation(null, phoneCreditDTO.getIbanOrder(), CreditMode.TELEPHONE, phoneCreditDTO.getBenefPhoneNumber(), beneficiary.getUser().getLastName(),beneficiary.getUser().getFirstName(),phoneCreditDTO.getAmount(),phoneCreditDTO.getMotif(), new Date() );
                                saveCreditOperation(creditOperation);
                            } catch (Exception e){
                                throw new SepaApplicationException("Failed to save the credit Transaction");
                            }
                        } else throw new SepaApplicationException("You can't credit yourself");
                    } else throw new SepaApplicationException("The amount of the transaction can't be upper than " + sepaConfiguration.getThresholdAmount()+ "  " + sepaConfiguration.getCurrency());
                } else throw new SepaApplicationException("The amount of the transaction can't be negative or equal to zero");
            } else throw new SepaApplicationException("The balance of your Account is insufficient to do the transaction ");

    }

    public List<CreditOperation> creditHistory(Long userId) throws SepaApplicationException {
        try{
            CompteDto compteDto = getAccountByUserId(userId);
            String ibanOrder = compteDto.getIban();
            return creditOperationRepository.findAllByIbanOrder(ibanOrder);
        } catch (Exception e){
            throw new SepaApplicationException("User with this id does not exist");
        }

    }

}
