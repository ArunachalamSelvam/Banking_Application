package com.banking.serviceImpl;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Address;
import com.banking.entities.Employee;
import com.banking.requestEntities.EmployeeWithAddress;
import com.banking.responseEntities.LoginResponse;
import com.banking.service.AddressService;
import com.banking.service.EmployeeService;
import com.banking.utils.BcryptUtil;

public class EmployeeServiceImpl implements EmployeeService {

	public final AddressService addressService = AddressServiceImpl.getInstance();

	public final DbManager DB_MANAGER = DbManager.getInstance();

	public static EmployeeServiceImpl instance = null;
	
	private final SecureRandom SECURE_RANDOM = new SecureRandom();
	
    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    
	private final BcryptUtil bcryptUtil = BcryptUtil.getInstance();




	private EmployeeServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	public static EmployeeServiceImpl getInstance() {
		if (instance == null) {
			instance = new EmployeeServiceImpl();
		}

		return instance;
	}

	@Override
	public Employee saveEmployee(EmployeeWithAddress employeeWithAddress) throws ClassNotFoundException, SQLException {

		Employee employee = employeeWithAddress.getEmployee();
		Address address = employeeWithAddress.getAddress();

		Address savedAddress = addressService.saveAddress(address);

		employee.setAddressId(savedAddress.getAddressId());

		String query = "INSERT INTO \"bankSchema\".employee(\n"
				+ "	first_name, middle_name, last_name, mobile_number, email_id, password, active_status, branch_id, address_id, father_name, mother_name, gender, role_id)\n"
				+ "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection connection = null;
		String password = generateRandomPassword();
		String hashedPassword = bcryptUtil.hashPin(password);
		
		try {

			connection = DB_MANAGER.createConnection();
			PreparedStatement pstmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, employee.getFirstName());
			pstmt.setString(2, employee.getMiddleName());
			pstmt.setString(3, employee.getLastName());
			pstmt.setString(4, employee.getMobileNumber());
			pstmt.setString(5, employee.getEmailId());
			pstmt.setString(6, password);
			pstmt.setBoolean(7, true);
			pstmt.setInt(8, employee.getBranchId());
			pstmt.setInt(9, employee.getAddressId());
			pstmt.setString(10, employee.getFatherName());
			pstmt.setString(11, employee.getMotherName());
			pstmt.setString(12, employee.getGender());
			pstmt.setInt(13, employee.getRoleId());


			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						employee.setEmpoyeeId(generatedKeys.getInt("employee_id"));
						employee.setEmployeeNo(generatedKeys.getInt("employee_no"));
					}
				}

				
			}

			else {
				return null;
			}

		}catch(Exception e) {
			e.printStackTrace();
			connection.close();
		}
		return employee;

	}

	@Override
	public Employee updateEmployee(Integer employeeId, EmployeeWithAddress employeeWithAddress)
			throws ClassNotFoundException, SQLException {

		if (isEmployeeExistsById(employeeId)) {
			Employee employee = employeeWithAddress.getEmployee();
			Address address = employeeWithAddress.getAddress();
			
			Integer addressId = getAddressIdByEmployeeId(employeeId);
			
			address.setAddressId(addressId);
			
			addressService.updateAddress(addressId, address);
			
			employee.setEmpoyeeId(employeeId);
			employee.setAddressId(addressId);
			
			String query = "UPDATE \"bankSchema\".employee\n"
					+ "	SET first_name=?, middle_name=?, last_name=?, mobile_number=?, email_id=?, password=?, active_status=?, branch_id=?, father_name=?, mother_name=?, gender=?, role_id=?\n"
					+ "	WHERE employee_id = ?";
			
			try(Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)){
				
				pstmt.setString(1, employee.getFirstName());
				pstmt.setString(2, employee.getMiddleName());
				pstmt.setString(3, employee.getLastName());
				pstmt.setString(4, employee.getMobileNumber());
				pstmt.setString(5, employee.getEmailId());
				pstmt.setString(6, employee.getPassword());
				pstmt.setBoolean(7, true);
				pstmt.setInt(8, employee.getBranchId());
				pstmt.setString(9, employee.getFatherName());
				pstmt.setString(10, employee.getMotherName());
				pstmt.setString(11, employee.getGender());
				pstmt.setInt(12, employee.getRoleId());
				pstmt.setInt(13, employeeId);

				int affectedRows = pstmt.executeUpdate();
				
				if (affectedRows > 0) {
					return employee;
				} else {
					throw new SQLException("Updating employee failed, no rows affected.");
				}


			}

		}

		return null;
	}

	@Override
	public List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException {
		String query = "SELECT * FROM \"bankSchema\".employee";
	   List<Employee> employees = new ArrayList<Employee>();
	    try (Connection connection = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {

	    	
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            Employee employee = new Employee();
	            employee.setEmpoyeeId(rs.getInt("employee_id"));
	            employee.setFirstName(rs.getString("first_name"));
	            employee.setMiddleName(rs.getString("middle_name"));
	            employee.setLastName(rs.getString("last_name"));
	            employee.setMobileNumber(rs.getString("mobile_number"));
	            employee.setEmailId(rs.getString("email_id"));
	            employee.setPassword(rs.getString("password"));
	            employee.setActiveStatus(rs.getBoolean("active_status"));
	            employee.setBranchId(rs.getInt("branch_id"));
	            employee.setAddressId(rs.getInt("address_id"));
	            employee.setFatherName(rs.getString("father_name"));
	            employee.setMotherName(rs.getString("mother_name"));
	            employee.setGender(rs.getString("gender"));
	            
	            employees.add(employee);
	        }
	    }
	    return employees;

	}

	@Override
	public Employee getEmployeeById(Integer employeeId) throws ClassNotFoundException, SQLException {
		
		String query = "SELECT * FROM \"bankSchema\".employee WHERE employee_id = ?";
	    Employee employee = null;
	    try (Connection connection = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {

	        pstmt.setInt(1, employeeId);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            employee = new Employee();
	            employee.setEmpoyeeId(rs.getInt("employee_id"));
	            employee.setFirstName(rs.getString("first_name"));
	            employee.setMiddleName(rs.getString("middle_name"));
	            employee.setLastName(rs.getString("last_name"));
	            employee.setMobileNumber(rs.getString("mobile_number"));
	            employee.setEmailId(rs.getString("email_id"));
	            employee.setPassword(rs.getString("password"));
	            employee.setActiveStatus(rs.getBoolean("active_status"));
	            employee.setBranchId(rs.getInt("branch_id"));
	            employee.setAddressId(rs.getInt("address_id"));
	            employee.setFatherName(rs.getString("father_name"));
	            employee.setMotherName(rs.getString("mother_name"));
	            employee.setGender(rs.getString("gender"));
	        }
	    }
	    return employee;
	}

	@Override
	public int deleteEmployee(Integer employeeId) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
		String query = "DELETE FROM \"bankSchema\".employee WHERE employee_id = ?";
	    
	    try (Connection connection = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = connection.prepareStatement(query)) {

	    	pstmt.setInt(1, employeeId);
	        return pstmt.executeUpdate();
	        
	    }

		
	}
	
	
	@Override
	public LoginResponse authenticateEmployee(String userName, String password) throws ClassNotFoundException, SQLException {
		
			String query = "SELECT \n" + "	e.customer_id AS customerId,\n" 
						+"e.first_name As firstName,"
						+"e.middle_name AS middleName,\n"
						+"e.last_name AS lastName,\n"
						+"FROM \"bankSchema\".employee e"
						+ "WHERE \n"
					+ "	e.email_id = ? AND e.password =?";

			LoginResponse response = new LoginResponse();
			
			try (Connection connection = DB_MANAGER.createConnection();
					PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, userName);
				pstmt.setString(2, password);

				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					
					response.setId(rs.getInt("customer_id"));
					response.setName(rs.getString("firstName") + " " + rs.getString("middleName") + " "
							+ rs.getString("lastName"));

					return response;

				}
			}
		
		return null;

	}

	public boolean isEmployeeExistsById(Integer employeeNo) throws ClassNotFoundException, SQLException {

		String query = "SELECT EXISTS (SELECT 1 FROM \"bankSchema\".employee WHERE employee_no = ?)";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setInt(1, employeeNo);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}

		}
		return false;

	}
	
	@Override
	public boolean isEmployeeExistsByUserNameANdPassword(String userName, String password) throws ClassNotFoundException, SQLException {

		String query = "SELECT EXISTS (SELECT 1 FROM \"bankSchema\".employee WHERE email_id = ? AND password = ?)";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setString(1, userName);
			pstmt.setString(2, password);


			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getBoolean(1);
			}

		}
		return false;

	}
	
	public Integer getAddressIdByEmployeeId(Integer employeeId) throws SQLException, ClassNotFoundException {
		
		String query = "SELECT address_id FROM \"bankSchema\".employee WHERE employee_id = ?";

		try (Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setInt(1, employeeId);

			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getInt("address_id");
			}
		}
		return null;
		
	}
	
	private String generateRandomPassword() {
       

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = SECURE_RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        System.out.println(password.toString());
        return password.toString();
    }

}
