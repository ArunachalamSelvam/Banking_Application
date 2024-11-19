package com.banking.entities;

public abstract class BankAccount {

	private Integer accountId;
	private String accountNumber;
	private Integer customerId;
	protected Double balance;
	protected Double minBalance;
	protected Boolean accountStatus;
	protected Integer branchId;
	
	public BankAccount() {
		// TODO Auto-generated constructor stub
	}
	

	public String getAccountNumber() {
		return accountNumber;
	}
	
	
	
	
	public BankAccount( Double balance, Double minBalance,
			Boolean accountStatus, Integer branchId) {
//		this.accountNumber = accountNumber;
//		this.customerId = customerId;
		this.balance = balance;
		this.minBalance = minBalance;
		this.accountStatus = accountStatus;
		this.branchId = branchId;
	}




	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(Double minBalance) {
		this.minBalance = minBalance;
	}

	public Boolean getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Boolean accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}



	public Integer getAccountId() {
		return accountId;
	}



	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	

	
}
