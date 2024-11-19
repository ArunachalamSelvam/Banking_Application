package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.entities.Customer;
import com.banking.requestEntities.CustomerWithAddress;
import com.banking.responseEntities.LoginResponse;
import com.banking.responseEntities.CustomerWithAddressResponse;

public interface CustomerService {

    Customer saveCustomer(CustomerWithAddress customerWithAddress) throws SQLException;

    Customer updateCustomer(Integer customerId, CustomerWithAddress customerWithAddress) throws ClassNotFoundException, SQLException;

    List<Customer> getAllCustomers();

    CustomerWithAddressResponse getCustomerById(Integer customerId);

    int deleteCustomer(Integer customerId);

	LoginResponse authenticateCustomer(String userName, String password)
			throws ClassNotFoundException, SQLException;

	boolean isCustomerExists(String userName, String password) throws SQLException, ClassNotFoundException;

	boolean updateAtmCardAddedStatus(int customerId, boolean status);
}
