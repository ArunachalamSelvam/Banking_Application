package com.banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Address;
import com.banking.entities.Bank;
import com.banking.entities.Branch;
import com.banking.entities.Customer;
import com.banking.entities.Employee;
import com.banking.entities.SavingsAccount;
import com.banking.entities.State;
import com.banking.entities.User;
import com.banking.requestEntities.CustomerWithAddress;
import com.banking.requestEntities.EmployeeWithAddress;
import com.banking.requestEntities.SavingsAccountWithCustomer;
import com.banking.responseEntities.CustomerWithAddressResponse;
import com.banking.service.UserService;
import com.banking.serviceImpl.BankServiceImpl;
import com.banking.serviceImpl.CustomerServiceImpl;
import com.banking.serviceImpl.EmployeeServiceImpl;
import com.banking.serviceImpl.SavingsAccountServiceImpl;
import com.banking.serviceImpl.StateServiceImpl;
import com.banking.serviceImpl.TransactionServiceImpl;
import com.banking.serviceImpl.UserServiceImpl;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		System.out.println("Banking Project");

//		Connection con = new DbManager().createConnection();
//		String query = "INSERT INTO \"bankSchema\".country (country_name, active_status) VALUES (?, ?)";
//		int generatedId = -1;
//		PreparedStatement pstmt = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS) ;
//
//            pstmt.setString(1, "India");
//            pstmt.setBoolean(2, true);
//
//            int affectedRows = pstmt.executeUpdate();
//
//            if (affectedRows > 0) {
//                try (ResultSet rs = pstmt.getGeneratedKeys()) {
//                    if (rs.next()) {
//                        generatedId = rs.getInt(1); // Retrieve the generated key
//                    }
//                }
//            }
//		System.out.println("Run");
//		System.out.println("generatedId : " + generatedId);

//		User user  = new User("Arunachalam","R","8870610967","arunachalamselvam6599@gmail.com","Arun@1103",true);
//		
//		User savedUser = new UserServiceImpl().saveUser(user);
//		
//		System.out.println(savedUser);

//		List<User> userList = new UserServiceImpl().getAllUsers();
//
//		for (User users : userList) {
//			System.out.println(users);
//		}

//		List<User> usersList = new UserServiceImpl().getAllUsers();
//
//		for (User users : usersList) {
//			System.out.println(users);
//		}

//		Bank savedBank = new BankServiceImpl().saveBank(new Bank("State Bank Of India"));
//		
//		System.out.println(savedBank);
//		
//		State savedState = new StateServiceImpl().saveState(new State(1,"TamilNadu",true));
//		
//		System.out.println();

//		CustomerWithAddress customerWithAddress = new CustomerWithAddress();
////		
//		Customer customer = new Customer("Arunachalam", "", "R", "Ramasamy", "Palavesam", "Male", "8870610967", "arunachalamselvam6599@gmail.com", "502550318496", "ESIPA3994P", null);
////		
//		Address address = new Address("1/219-2, muppudathi amman kovil st", 1, 1, true);
//		
//		customer.setAddressId(address.getAddressId());
//		customerWithAddress.setCustomer(customer);
//		customerWithAddress.setAddress(address);
		
//		Customer savedCustomer = CustomerServiceImpl.getInstance().saveCustomer(customerWithAddress);
//		
//		System.out.println(savedCustomer);
		
//		CustomerWithAddressResponse customer = CustomerServiceImpl.getInstance().getCustomerById(1);
		
//		System.out.println(customer);
		
		
//		CustomerWithAddressResponse customer1 = CustomerServiceImpl.getInstance().getCustomerById(1);
		
//		System.out.println("customer 1 : "+customer1);
		
		
		
//		SavingsAccountWithCustomer savingsAcWithCustomer = new SavingsAccountWithCustomer();
//		
//		SavingsAccount savingsAccount = new SavingsAccount(200000.0, 1500.0, true, 1, 3.50);
//		
//		savingsAcWithCustomer.setCustomer(customer);
//		savingsAcWithCustomer.setAddress(address);
//		savingsAcWithCustomer.setSavingsAccount(savingsAccount);
//		
//		SavingsAccountServiceImpl service = SavingsAccountServiceImpl.getInstance();
//		
//		SavingsAccount savingsAc = service.saveSavingsAccount(savingsAcWithCustomer);
//		
//		System.out.println(savingsAc);
		
//		SavingsAccount savingsAc = service.updateSavingsAccount(3, savingsAcWithCustomer);
//		
//		System.out.println(savingsAc);
		
//		Employee employee = new Employee("Arunachalam", "", "R", "male", "Ramasamy A", "palavesam R", "8870610967", "arunachalamselvam9965@gmail.com", "Arun@1103", true, 1, null);
//		
////		Employee employee = new Employee("Arunachalam", "", "R", "male", "Ramasamy", "Palaveam", "8870610967", "arunachalamselvam6599@gmail.com", , null, null, null, null)
//		
//		EmployeeWithAddress employeeWithAddress = new EmployeeWithAddress();
//		employeeWithAddress.setEmployee(employee);
//		employeeWithAddress.setAddress(address);
////		
//		EmployeeServiceImpl employeeServiceImpl = EmployeeServiceImpl.getInstance();
//		
//		Employee savedEmployee = employeeServiceImpl.saveEmployee(employeeWithAddress);
		
//		Employee updateEmployee = employeeServiceImpl.updateEmployee(1, employeeWithAddress);
//		
//		System.out.println(savedEmployee);
		

//		System.out.println(employeeServiceImpl.isEmployeeExistsById(3));
		
//		TransactionServiceImpl service = TransactionServiceImpl.getInstance();
//		service.withdraw(2, 1000.00);
//		
		
//		EmployeeServiceImpl employeeService = EmployeeServiceImpl.getInstance();
//		
//		System.out.println(employeeService.getEmployeeById(1));
		
		
		

	}

}
