package com.ensicaen.sepa.controller;

import com.ensicaen.sepa.dtos.CompteDto;
import com.ensicaen.sepa.dtos.IbanCreditDto;
import com.ensicaen.sepa.dtos.PhoneCreditDTO;
import com.ensicaen.sepa.exceptions.SepaApplicationException;
import com.ensicaen.sepa.models.Account;
import com.ensicaen.sepa.models.CreditOperation;
import com.ensicaen.sepa.services.AccountService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@ResponseBody
@RequestMapping("/comptes")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/")
    public List<Account> hello(){
        return accountService.getListAccount();
    }


    @PostMapping(path = "/signIn")
    public CompteDto signIn(@RequestBody String email) throws SepaApplicationException {
        System.out.println(email);
       return accountService.getAccountByEmail(email);
    }

    @GetMapping(path = "/user/home/{userID}")
    public CompteDto Home(@PathVariable Long userID) throws SepaApplicationException {
        return accountService.getAccountByUserId(userID);
    }
    @PostMapping(path = "/iban/credit")
    public void creditAccount(@RequestBody @NotNull IbanCreditDto ibanCreditDto) throws SepaApplicationException {
        accountService.creditAccountByIban(ibanCreditDto);
    }

    @PostMapping(path = "/phone/credit")
    public void creditAccountWithNumber(@RequestBody @NotNull PhoneCreditDTO phoneCreditDTO) throws SepaApplicationException {
        accountService.creditAccountByPhoneNumber(phoneCreditDTO);
    }
    @GetMapping(path = "/credit/history/{userID}")
    public List<CreditOperation> creditHistory(@PathVariable Long userID) throws SepaApplicationException {
        return accountService.creditHistory(userID);
    }

}
