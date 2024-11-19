package com.banking.controller;


import com.banking.entities.Employee;
import com.banking.entities.Address;
import com.banking.requestEntities.EmployeeWithAddress;
import com.banking.serviceImpl.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet {

    private final EmployeeServiceImpl employeeService = EmployeeServiceImpl.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	System.out.println(request.getMethod());
        String employeeId = request.getParameter("id");
        
        if (employeeId != null) {
        	try {
		    Employee employee = employeeService.getEmployeeById(Integer.parseInt(employeeId));
		    response.getWriter().write(objectMapper.writeValueAsString(employee));
        	}catch(Exception e)
        	{
        		e.printStackTrace();
        	}
		} else {
			try {
		    List<Employee> employees = employeeService.getAllEmployees();
		    response.getWriter().write(objectMapper.writeValueAsString(employees));
			}catch(Exception e)
        	{
        		e.printStackTrace();
        	}
		}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EmployeeWithAddress employeeWithAddress = objectMapper.readValue(request.getInputStream(), EmployeeWithAddress.class);
        try {
            Employee savedEmployee = employeeService.saveEmployee(employeeWithAddress);
            response.getWriter().write(objectMapper.writeValueAsString(savedEmployee));
        } catch (SQLException | ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeId = request.getParameter("id");
        EmployeeWithAddress employeeWithAddress = objectMapper.readValue(request.getInputStream(), EmployeeWithAddress.class);
        try {
            Employee updatedEmployee = employeeService.updateEmployee(Integer.parseInt(employeeId), employeeWithAddress);
            response.getWriter().write(objectMapper.writeValueAsString(updatedEmployee));
        } catch (SQLException | ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String employeeId = request.getParameter("id");
        int result = 0;
		try {
			result = employeeService.deleteEmployee(Integer.parseInt(employeeId));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().write("{\"result\":\"" + result + "\"}");
    }
    
    protected void login() {
    	System.out.println("it's working...");
    }
}
