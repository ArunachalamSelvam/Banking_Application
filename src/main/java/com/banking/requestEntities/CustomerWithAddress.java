package com.banking.requestEntities;

import com.banking.entities.Address;
import com.banking.entities.Customer;

public class CustomerWithAddress {
	
	private Customer customer;
	private Address address;
	
	
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
	
	

}
