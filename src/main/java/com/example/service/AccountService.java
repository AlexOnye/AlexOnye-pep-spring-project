package com.example.service;

import com.example.entity.Account;
import com.example.exception.InvalidAccountException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account acc) {
        if (acc.getUsername() == null || acc.getUsername().trim().isEmpty()) {
            throw new InvalidAccountException("Username must not be blank.");
        }
        if (acc.getPassword() == null || acc.getPassword().length() <= 4) {
            throw new InvalidAccountException("Password must be longer than 4 characters.");
        }
        if (accountRepository.findByUsername(acc.getUsername()).isPresent()) {
            throw new InvalidAccountException("Username already exists.");
        }

        return accountRepository.save(acc);
    }

    public Account login(Account acc) {
        return accountRepository.findByUsername(acc.getUsername())
                .filter(a -> a.getPassword().equals(acc.getPassword()))
                .orElseThrow(() -> new InvalidAccountException("Invalid credentials"));
    }
}
