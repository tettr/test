package com.fullstackbackend.controller;

import com.fullstackbackend.exception.NotFoundException;
import com.fullstackbackend.model.Account;
import com.fullstackbackend.DAO.*;
import com.fullstackbackend.model.PhoneData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import static com.fullstackbackend.controller.UserController.idForUser;

@RestController
@CrossOrigin("http://localhost:3000")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 30000) // Каждые 30 секунд
    public void updateBalances() {
        List<Account> accList = accountRepository.findAll();
        for (Account account : accList) {
            account.increaseBalance();
            accountRepository.findById(account.getId())
                    .map(order -> {
                        order.setBalance(account.getBalance());
                        return null;
                    }).orElseThrow(() -> new NotFoundException(account.getId()));
        }
    }

    @PutMapping("/userNewBalance/{id}")
    public void transaction(@RequestBody Integer idFortran, BigDecimal value, @PathVariable Integer id) {
        if(accountRepository.findByUser(id).get().getBalance().compareTo(value) != -1) {
            accountRepository.findByUser(id)
                    .map(account -> {
                        account.setBalance(account.getBalance().subtract(value));
                        return accountRepository.save(account);
                    }).orElseThrow(() -> new NotFoundException(id));

            accountRepository.findByUser(idFortran)
                    .map(account -> {
                        account.setBalance(account.getBalance().add(value));
                        return accountRepository.save(account);
                    }).orElseThrow(() -> new NotFoundException(id));
        }
    }
}
