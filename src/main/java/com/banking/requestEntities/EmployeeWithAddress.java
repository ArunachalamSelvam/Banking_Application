package com.banking.requestEntities;

import com.banking.entities.Address;
import com.banking.entities.Employee;

public class EmployeeWithAddress {
	
	private Employee employee;
	private Address address;
	
	public EmployeeWithAddress() {
		// TODO Auto-generated constructor stub
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	

}
