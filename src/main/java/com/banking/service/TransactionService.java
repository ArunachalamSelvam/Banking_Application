package com.banking.service;

import com.banking.entities.Transaction;
import com.banking.entities.TransactionType;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.exception.BalanceInsufficientExeception;
import com.banking.exception.InvalidDepositAmountException;

import java.sql.SQLException;
import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactionHistory(Integer customerId);
//    boolean addTransaction(Transaction transaction);
    boolean transaction(String sourceAccountNo,String receiverAccountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException;
    boolean deposit(String accountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException, InvalidDepositAmountException;
    boolean withdraw(String accountNo, Double amount) throws SQLException, ClassNotFoundException, AccountNumberNotExistException;
	List<Transaction> getTransactionHistoryByCustomerId(Integer customerId, String startDate, String endDate);
	List<Transaction> getTransactionHistoryByCustomerId(Integer customerId);
	boolean deposit(Integer customerId, Double amount)
			throws SQLException, ClassNotFoundException, AccountNumberNotExistException;
	boolean withdraw(Integer customerId, Double amount)
			throws SQLException, ClassNotFoundException, AccountNumberNotExistException, BalanceInsufficientExeception;
	boolean transactionToReceiverAccountNoByCustomerId(Integer customerId, String receiverAccountNo, Double amount)
			throws SQLException, ClassNotFoundException, AccountNumberNotExistException, BalanceInsufficientExeception;

   
}
