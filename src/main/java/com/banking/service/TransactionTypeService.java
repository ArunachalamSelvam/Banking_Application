package com.banking.service;

import java.util.List;

import com.banking.entities.TransactionType;

public interface TransactionTypeService {

	List<TransactionType> getAllTransactionTypes();

	boolean addTransactionType(TransactionType transactionType);
}
