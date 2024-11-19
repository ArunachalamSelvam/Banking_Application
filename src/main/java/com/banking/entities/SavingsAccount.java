package com.banking.entities;

public class SavingsAccount extends BankAccount {

	private Double interestRate;

//	private Boolean accountStatus;

	public SavingsAccount() {
        super();
    }
	public SavingsAccount(Double balance, Double minBalance,
			Boolean accountStatus, Integer branchId, Double interestRate) {
		super(balance, minBalance, accountStatus, branchId);
		this.interestRate = interestRate;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}
	
	@Override
    public String toString() {
        return String.format("BankAccount [balance=%.2f, minBalance=%.2f, accountStatus=%s, branchId=%d]",
                balance, minBalance, accountStatus, branchId);
    }

}
