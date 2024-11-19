package com.banking.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONObject;

import com.banking.exception.InvalidCustomerException;
import com.banking.serviceImpl.CustomerServiceImpl;

/**
 * Servlet implementation class CustomerVerifyServlet
 */
public class CustomerVerifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final CustomerServiceImpl customerService = CustomerServiceImpl.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerVerifyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Inside Customer Verify Servlet");
		// TODO Auto-generated method stub
		StringBuilder jsonBuffer = new StringBuilder();
		String line;
		try (BufferedReader reader = request.getReader()) {
			while ((line = reader.readLine()) != null) {
				jsonBuffer.append(line);
			}
		}

		// Parse the JSON data
		String jsonData = jsonBuffer.toString();
		JSONObject jsonObject = new JSONObject(jsonData);

		// Extract fields from the JSON
//		String action = jsonObject.getString("action");
		String email = jsonObject.getString("email");
		
		try {
			boolean isValidCustomer = customerService.isValidCustomer(email);
			if(isValidCustomer) {
				System.out.println("Customer valid");
//				response.getWriter().write("{\"response\" : \"Cutomer verified.\"}");
				RequestDispatcher rd = request.getRequestDispatcher("OtpServlet");
				rd.forward(request, response);
				
			}
		}catch (InvalidCustomerException e) {
			response.getWriter().write("{\"Error\" : \"Cutomer not found.\"}");
			e.printStackTrace();
		}catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
