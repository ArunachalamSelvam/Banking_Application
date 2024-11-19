package com.banking.entities;

import java.util.Date;

public class Transaction {
    private Integer transactionId;
    private Integer sourceAccountId;
    private Integer destinationAccountId; // Destination account for deposit
    private Double amount;
    private Date transactionDate;
    private Integer transactionTypeId;
    private String description;
    private Double available_balance;
    
    public Transaction() {
    	this.transactionDate = new Date();
	}
    // Getters and setters
    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(Integer customerId) {
        this.sourceAccountId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }
	public Integer getDestinationAccountId() {
		return destinationAccountId;
	}
	public void setDestinationAccountId(Integer destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getBalance() {
		return available_balance;
	}
	public void setBalance(Double balance) {
		this.available_balance = balance;
	}
}
