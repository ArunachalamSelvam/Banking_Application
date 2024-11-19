package com.banking.serviceImpl;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.ATMCard;
import com.banking.entities.Address;
import com.banking.entities.Customer;
import com.banking.exception.InvalidCustomerException;
import com.banking.requestEntities.CustomerWithAddress;
import com.banking.responseEntities.LoginResponse;
import com.banking.responseEntities.CustomerWithAddressResponse;
import com.banking.service.CustomerService;

import jakarta.servlet.http.HttpSession;

public class CustomerServiceImpl implements CustomerService {

	private static final DbManager DB_MANAGER = DbManager.getInstance();

	private static final AddressServiceImpl addressService = AddressServiceImpl.getInstance();

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private static CustomerServiceImpl instance = null;

	private CustomerServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public static CustomerServiceImpl getInstance() {
		if (instance == null) {
			instance = new CustomerServiceImpl();
		}

		return instance;
	}

	@Override
	public Customer saveCustomer(CustomerWithAddress customerWithAddress) throws SQLException {

		Customer customer = customerWithAddress.getCustomer();
		

		try {

			String insertQuery = "INSERT INTO \"bankSchema\".customer (first_name, middle_name, last_name, father_name, mother_name, "
					+ "gender, mobile_number, email_id, adhar_number, pan_number, address_id,password, role_id) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

			Connection con = DB_MANAGER.createConnection();
			PreparedStatement pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			Address address = customerWithAddress.getAddress();

			Address savedAddress = addressService.saveAddress(address);

			System.out.println(savedAddress);

			Integer addressId = savedAddress.getAddressId();

			customer.setAddressId(addressId);
			
			System.out.println(customer.toString());

			pstmt.setString(1, customer.getFirstName());
			pstmt.setString(2, customer.getMiddleName());
			pstmt.setString(3, customer.getLastName());
			pstmt.setString(4, customer.getFatherName());
			pstmt.setString(5, customer.getMotherName());
			pstmt.setString(6, customer.getGender());
			pstmt.setString(7, customer.getMobileNumber());
			pstmt.setString(8, customer.getEmailId());
			pstmt.setString(9, customer.getAdharNumber());
			pstmt.setString(10, customer.getPanNumber());
			pstmt.setInt(11, customer.getAddressId());
			pstmt.setString(12, customer.getPassword());
			pstmt.setInt(13, customer.getRoleId());
//			pstmt.setBoolean(14, customer.getIsAtrmCardAdded());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						System.out.println("customerId:" + generatedKeys.getInt(16));
						customer.setCustomerId(generatedKeys.getInt(16));
						System.out.println("Saved Customer ID : " + customer.getCustomerId());
					}
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// Handle exception as needed
		}

		return customer;
	}

	@Override
	public Customer updateCustomer(Integer customerId, CustomerWithAddress customerWithAddress)
			throws ClassNotFoundException, SQLException {

		if (isCustomerIdExists(customerId)) {

			Customer customer = customerWithAddress.getCustomer();
			Integer addressId = getAddressIdByCustomerId(customerId);

			customer.setCustomerId(customerId);
			customer.setAddressId(addressId);

			Address address = customerWithAddress.getAddress();
			address.setAddressId(addressId);

			addressService.updateAddress(addressId, address);

			String query = "UPDATE \"bankSchema\".customer\n"
					+ "	SET  first_name=?, middle_name=?, last_name=?, father_name=?, mother_name=?, gender=?, mobile_number=?, email_id=?, adhar_number=?, pan_number=?, address_id=?\n"
					+ "	WHERE customer_id = ?";

			try (Connection connection = DB_MANAGER.createConnection();
					PreparedStatement pstmt = connection.prepareStatement(query)) {

				pstmt.setString(1, customer.getFirstName());
				pstmt.setString(2, customer.getMiddleName());
				pstmt.setString(3, customer.getLastName());
				pstmt.setString(4, customer.getFatherName());
				pstmt.setString(5, customer.getMotherName());
				pstmt.setString(6, customer.getGender());
				pstmt.setString(7, customer.getMobileNumber());
				pstmt.setString(8, customer.getEmailId());
				pstmt.setString(9, customer.getAdharNumber());
				pstmt.setString(10, customer.getPanNumber());
				pstmt.setInt(11, addressId);
				pstmt.setInt(12, customerId);

				int affectedRows = pstmt.executeUpdate();

				if (affectedRows > 0) {
					return customer;
				} else {
					throw new SQLException("Updating customer failed, no rows affected.");
				}

			}

		}
		return null;

	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		String selectQuery = "SELECT * FROM \"bankSchema\".customer";

		try (Connection con = DB_MANAGER.createConnection();
				PreparedStatement pstmt = con.prepareStatement(selectQuery);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerId(rs.getInt("customer_id"));
				customer.setFirstName(rs.getString("first_name"));
				customer.setMiddleName(rs.getString("middle_name"));
				customer.setLastName(rs.getString("last_name"));
				customer.setFatherName(rs.getString("father_name"));
				customer.setMotherName(rs.getString("mother_name"));
				customer.setGender(rs.getString("gender"));
				customer.setMobileNumber(rs.getString("mobile_number"));
				customer.setEmailId(rs.getString("email_id"));
				customer.setAdharNumber(rs.getString("adhar_number"));
				customer.setPanNumber(rs.getString("pan_number"));
				customer.setAddressId(rs.getInt("address_id"));
				customers.add(customer);
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// Handle exception as needed
		}

		return customers;
	}

	@Override
	public CustomerWithAddressResponse getCustomerById(Integer customerId) {
		CustomerWithAddressResponse customer = new CustomerWithAddressResponse();

		String selectQuery = "SELECT c.customer_id AS customerId,\n" + "	c.first_name AS firstName, \n"
				+ "	c.middle_name As middleName, \n" + "	c.last_name AS lastName, \n"
				+ "	c.father_name AS fatherName, \n" + "	c.mother_name AS motherName, \n"
				+ "	c.gender AS gender, \n" + "	c.mobile_number AS mobileNumber, \n" + "	c.email_id AS emailId, \n"
				+ "	c.adhar_number AS adharNumber, \n" + "	c.pan_number As panNumber, \n"
				+ "	a.address_id As addressId,\n" + "	a.address AS address, \n" + "	co.country_id AS countryId,\n"
				+ "	co.country_name AS countryName,\n" + "	s.state_id AS stateId,\n" + "	s.state_name AS stateNmae\n"
				+ "	\n" + "	FROM \"bankSchema\".customer c\n"
				+ "	JOIN \"bankSchema\".address a ON c.address_id = a.address_id\n"
				+ "	JOIN \"bankSchema\".country co ON a.country_id = co.country_id\n"
				+ "	JOIN \"bankSchema\".state s ON a.state_id = s.state_id\n" + "	Where c.customer_id = ?";

		try (Connection con = DB_MANAGER.createConnection();
				PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {

					customer.setCustomerId(rs.getInt("customerId"));
					customer.setFirstName(rs.getString("firstName"));
					customer.setMiddleName(rs.getString("middleName"));
					customer.setLastName(rs.getString("lastName"));
					customer.setFatherName(rs.getString("fatherName"));
					customer.setMotherName(rs.getString("motherName"));
					customer.setGender(rs.getString("gender"));
					customer.setMobileNumber(rs.getString("mobileNumber"));
					customer.setEmailId(rs.getString("emailId"));
					customer.setAdharNumber(rs.getString("adharNumber"));
					customer.setPanNumber(rs.getString("panNumber"));
					customer.setAddressId(rs.getInt("addressId"));

					customer.setAddress(rs.getString("address"));
					customer.setCountryId(rs.getInt("countryId"));
					customer.setStateId(rs.getInt("stateId"));
					customer.setCountryName(rs.getString("countryName"));
					customer.setStateName(rs.getString("stateName"));

				}

			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// Handle exception as needed
		}

		return customer;
	}

	@Override
	public int deleteCustomer(Integer customerId) {
		String deleteQuery = "DELETE FROM \"bankSchema\".customer WHERE customer_id = ?";

		try (Connection con = DB_MANAGER.createConnection();
				PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

			pstmt.setInt(1, customerId);
			return pstmt.executeUpdate();

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			// Handle exception as needed
		}

		return 0;
	}

	public boolean isCustomerIdExists(Integer customerId) throws SQLException, ClassNotFoundException {
		String query = "SELECT EXISTS (SELECT 1 FROM \"bankSchema\".customer WHERE customer_id = ?)";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public Integer getAddressIdByCustomerId(Integer customerId) throws SQLException, ClassNotFoundException {
		String query = "SELECT address_id FROM \"bankSchema\".customer WHERE customer_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, customerId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("address_id");
			} else {
				return null; // or throw an exception if a customer with the given ID should always exist
			}

		}
	}

//	@Override
//	public CustomerLoginResponse authenticateCustomer(String userName, String password)
//			throws ClassNotFoundException, SQLException {
//		if (isCustomerExists(userName, password)) {
//			String query = "SELECT \n" + "	c.customer_id AS customerId,\n" + "	c.first_name As firstName,\n"
//					+ "	c.middle_name AS middleName,\n" + "	c.last_name AS lastName,\n"
//					+ "	c.mobile_number AS mobileNumber,\n" + "	c.email_id AS emailId,\n"
//					+ "	c.address_id AS addressId,\n" + "	c.role_id AS roleId,\n" + "	ac.account_id AS accountId,\n"
//					+ "	ac.account_number AS accountNo,\n" + "	ac.branch_id AS branchId\n" + "FROM\n"
//					+ "	\"bankSchema\".customer c\n" + "	\n" + "JOIN\n"
//					+ "	\"bankSchema\".savings_account ac  ON c.customer_id = ac.customer_id\n" + "WHERE \n"
//					+ "	c.email_id = ? AND c.password =?";
//
//			try (Connection connection = DB_MANAGER.createConnection();
//					PreparedStatement pstmt = connection.prepareStatement(query)) {
//				pstmt.setString(1, userName);
//				pstmt.setString(2, password);
//
//				ResultSet rs = pstmt.executeQuery();
//				if (rs.next()) {
//					CustomerLoginResponse response = new CustomerLoginResponse();
//					response.setName(rs.getString("firstName") + " " + rs.getString("middleName") + " "
//							+ rs.getString("lastName"));
//					response.setCustomerId(rs.getInt("customerId"));
//					response.setName(rs.getString("firstName") + " " + rs.getString("middleName") + " "
//							+ rs.getString("lastName"));
//					response.setMobileNumber(rs.getString("mobileNumber"));
//					response.setEmailId(rs.getString("emailId"));
//					response.setAddressId(rs.getInt("addressId"));
//					response.setRoleId(rs.getInt("roleId"));
//					response.setAccountId(rs.getInt("accountId"));
//					response.setAccountNumber(rs.getString("accountNo"));
//					response.setBranchId(rs.getInt("branchId"));
//
//					return response;
//
//				}
//			}
//		}
//		return null;
//	}

	@Override
	public LoginResponse authenticateCustomer(String userName, String password)
	        throws ClassNotFoundException, SQLException {

	    String query = "SELECT \n"
	            + "    c.customer_id AS customerId,\n"
	            + "    c.first_name AS firstName,\n"
	            + "    c.middle_name AS middleName,\n"
	            + "    c.last_name AS lastName\n"  // Added comma here
	            + "FROM \n"
	            + "    \"bankSchema\".customer c\n"
	            + "WHERE \n"
	            + "    c.email_id = ?\n"
	            + "    AND c.password = ?";

	    LoginResponse response = new LoginResponse();

	    try (Connection connection = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        pstmt.setString(2, password);

	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            response.setId(rs.getInt("customerId"));
	            response.setName(rs.getString("firstName") + " " + rs.getString("middleName") + " "
	                    + rs.getString("lastName"));
	          

	            return response;
	        }
	    }

	    return null;
	}
	
	@Override
	public boolean isCustomerExists(String userName, String password) throws SQLException, ClassNotFoundException {
		String query = "SELECT COUNT(1) FROM \"bankSchema\".customer WHERE email_id = ? AND password = ?";

		try (Connection con = DB_MANAGER.createConnection(); PreparedStatement pstmt = con.prepareStatement(query)) {

			pstmt.setString(1, userName);
			pstmt.setString(2, password);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Returns true if a matching customer is found
				}
			}
		}

		return false; // Returns false if no matching customer is found

	}
	
	public boolean isValidCustomer(String email) throws SQLException, ClassNotFoundException, InvalidCustomerException {
		String query = "SELECT COUNT(1) FROM \"bankSchema\".customer WHERE email_id = ?";
		
		try(Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, email);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // Returns true if a matching customer is found
				}else {
					throw new InvalidCustomerException("Customer Not Found");
				}
			}
		}
		
	}
	
	
	public boolean resetPassword(String email, String newPassword) throws ClassNotFoundException, SQLException {
		System.out.println("Inside resetPassword CustomerServiceImpl");
		System.out.println("email : " + email);
		System.out.println("password : " + newPassword);
		String query = "UPDATE \"bankSchema\".customer SET password=? WHERE email_id = ?";
		try(Connection connection = DB_MANAGER.createConnection(); PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setString(1, newPassword);
			pstmt.setString(2, email);
			
			int rowsUpdated = pstmt.executeUpdate();
			System.out.println("is Updated : " + rowsUpdated);
			if(rowsUpdated>0) {
				return true;
			}else {
				throw new SQLException("Updating customer failed, no rows affected.");

			}
		}
	}
	
	@Override
	public boolean updateAtmCardAddedStatus(int customerId, boolean status) {
		String query = "UPDATE \"bankSchema\".customer\n"
						+"SET is_card_added = ?\n"
						+"WHERE customer_id = ?";
		
		try(Connection connection = DB_MANAGER.createConnection(); PreparedStatement pstmt = connection.prepareStatement(query)){
			pstmt.setBoolean(1, status);
			pstmt.setInt(2, customerId);
			
			ResultSet rs = pstmt.executeQuery();
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				return true;
			} else {
				throw new SQLException("Updating customer failed, no rows affected.");

			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
