package com.banking.entities;

public class Bank {

	private Integer bankId;

	private String bankName;
	
	public Bank() {
		// TODO Auto-generated constructor stub
	}

	public Bank(String bankName) {

		this.bankName = bankName;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	 @Override
	    public String toString() {
	        return "Bank{" +
	                "bankId=" + bankId + "\n" +
	                ", bankName='" + bankName + '\'' +
	                '}';
	    }
}
