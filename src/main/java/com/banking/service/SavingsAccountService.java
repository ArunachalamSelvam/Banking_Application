package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.entities.SavingsAccount;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.requestEntities.SavingsAccountWithCustomer;

public interface SavingsAccountService {

    String saveSavingsAccount(SavingsAccountWithCustomer savingsAccountWithCustomer) throws SQLException, ClassNotFoundException;

    SavingsAccount updateSavingsAccount(Integer accountId, SavingsAccountWithCustomer savingsAccountWithCustomer) throws ClassNotFoundException, SQLException;

    List<SavingsAccount> getAllSavingsAccounts();

    SavingsAccount getSavingsAccountByAccountNo(String accountNo) throws AccountNumberNotExistException;

    int deleteSavingsAccount(Integer accountId);

	Double getBalanceByCustomerId(Integer customerId);

	Double getMinBalanceByCustomerId(Integer customerId);
}
