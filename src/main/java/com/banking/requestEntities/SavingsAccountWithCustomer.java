package com.banking.requestEntities;

import java.util.List;

import com.banking.entities.Address;
import com.banking.entities.Answer;
import com.banking.entities.Customer;
import com.banking.entities.SavingsAccount;

public class SavingsAccountWithCustomer {
	
	private SavingsAccount savingsAccount;
	private Customer customer;
	private Address address;
	private boolean isAtmCardNeeded;
	private String atmPin;
//	private List<Answer> answers;
	
	
	
	public SavingsAccount getSavingsAccount() {
		return savingsAccount;
	}
	public void setSavingsAccount(SavingsAccount savingsAccount) {
		this.savingsAccount = savingsAccount;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public boolean isAtmCardNeeded() {
		return isAtmCardNeeded;
	}
	public void setAtmCardNeeded(boolean isAtmCardNeeded) {
		this.isAtmCardNeeded = isAtmCardNeeded;
	}
	public String getAtmPin() {
		return atmPin;
	}
	public void setAtmPin(String atmPin) {
		this.atmPin = atmPin;
	}
//	public List<Answer> getAnswers() {
//		return answers;
//	}
//	public void setAnswers(List<Answer> answers) {
//		this.answers = answers;
//	}
	
	
	
	

}
