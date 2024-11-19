package com.banking.service;

import java.util.List;

import com.banking.entities.Bank;

public interface BankService {

    Bank saveBank(Bank bank);

    Bank updateBank(Integer bankId, Bank bank);

    List<Bank> getAllBanks();

    Bank getBankById(Integer bankId);

    int deleteBank(Integer bankId);
}
