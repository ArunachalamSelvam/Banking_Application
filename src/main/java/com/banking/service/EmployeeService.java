package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.entities.Employee;
import com.banking.requestEntities.EmployeeWithAddress;
import com.banking.responseEntities.LoginResponse;

public interface EmployeeService {

    Employee saveEmployee(EmployeeWithAddress employeeWithAddress) throws ClassNotFoundException, SQLException;

    Employee updateEmployee(Integer employeeId, EmployeeWithAddress employeeWithAddress) throws ClassNotFoundException, SQLException;

    List<Employee> getAllEmployees() throws SQLException, ClassNotFoundException;

    Employee getEmployeeById(Integer employeeId) throws ClassNotFoundException, SQLException;

    int deleteEmployee(Integer employeeId) throws ClassNotFoundException, SQLException;

	boolean isEmployeeExistsByUserNameANdPassword(String userName, String password)
			throws ClassNotFoundException, SQLException;

	LoginResponse authenticateEmployee(String userName, String password) throws ClassNotFoundException, SQLException;
}
