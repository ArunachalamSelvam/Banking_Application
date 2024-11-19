package com.banking.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.banking.entities.Transaction;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.exception.BalanceInsufficientExeception;
import com.banking.exception.InvalidDepositAmountException;
import com.banking.requestEntities.TransactionRequest;
import com.banking.service.TransactionService;
import com.banking.serviceImpl.SavingsAccountServiceImpl;
import com.banking.serviceImpl.TransactionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

@WebServlet("/api/TransactionServlet")
public class TransactionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private TransactionService transactionService;
	
	private SavingsAccountServiceImpl savingsAccountService;

	
    private final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public void init() throws ServletException {
		transactionService = TransactionServiceImpl.getInstance(); // Assuming TransactionServiceImpl is implemented
		savingsAccountService = SavingsAccountServiceImpl.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false); // Check if a session already exists
		
		if (session==null || session.getAttribute("customer_id") == null) {
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			response.sendRedirect("login.html");
			
			return;
		}
//		
		Integer customerId = Integer.parseInt(session.getAttribute("customer_id").toString());

		
		
//		StringBuilder jsonString = new StringBuilder();
//		try (BufferedReader reader = request.getReader()) {
//			String line;
//			while ((line = reader.readLine()) != null) {
//				jsonString.append(line);
//			}
//		}
//
//		// Parse JSON request body
//		JSONObject jsonObject = new JSONObject(jsonString.toString());
//		String startDate = jsonObject.getString("startDate");
//		String endDate = jsonObject.getString("endDate");
		
		 String startDate = request.getParameter("startDate");
		    String endDate = request.getParameter("endDate");

		// Fetch transaction history from the service
		List<Transaction> transactions = transactionService.getTransactionHistoryByCustomerId(customerId, startDate,
				endDate);

		// Convert list of transactions to JSON
		JSONArray jsonArray = new JSONArray();
		for (Transaction transaction : transactions) {
			
			JSONObject transactionJson = new JSONObject();
			transactionJson.put("transactionId", transaction.getTransactionId());
			transactionJson.put("sourceAccountId", transaction.getSourceAccountId());
			transactionJson.put("receiverAccountId", transaction.getDestinationAccountId());
			transactionJson.put("amount", transaction.getAmount());
			transactionJson.put("transactionTypeId", transaction.getTransactionTypeId());
			transactionJson.put("description", transaction.getDescription());
			transactionJson.put("transactionDate", transaction.getTransactionDate().toString());
			transactionJson.put("availableBalance", transaction.getBalance());
			jsonArray.put(transactionJson);
		}

		// Set response content type to JSON
		JSONObject responseJson = new JSONObject();
        responseJson.put("transactions", jsonArray);

        // Set response content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the JSON response
        PrintWriter out = response.getWriter();
        out.print(responseJson.toString());
        out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		System.out.println(session.getAttribute("customer_id"));
		String action = request.getParameter("action");

		try {
			switch (action) {
			case "deposit":
				deposit(request, response);
				break;
			case "withdraw":
				withdraw(request, response);
				break;
			case "transaction":
				transaction(request, response);
				break;
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
				break;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new ServletException(e);
		} catch (AccountNumberNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void deposit(HttpServletRequest request, HttpServletResponse response)
	        throws SQLException, ClassNotFoundException, IOException, AccountNumberNotExistException {

	    String accountNo = request.getParameter("accountNo");
	    HttpSession session = request.getSession(false);
	    Integer customerId = null;
	    
	    if (session != null && session.getAttribute("customer_id") != null) {
	        customerId = Integer.parseInt(session.getAttribute("customer_id").toString());
	    }

	    double amount = Double.parseDouble(request.getParameter("amount"));
	    boolean success = false;

	    if (accountNo == null && customerId != null) {
	        success = transactionService.deposit(customerId, amount);
	    } else if (accountNo != null && customerId == null) {
	        try {
				success = transactionService.deposit(accountNo, amount);
			}catch (InvalidDepositAmountException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AccountNumberNotExistException e) {
				 response.getWriter().println("Deposit Amount should be greater than zero.");
				e.printStackTrace();
			} 
	    } else {
	        response.getWriter().println("Invalid deposit request!");
	        return;
	    }

	    if (success) {
	        response.getWriter().println("Deposit successful!");
	    } else {
	        response.getWriter().println("Deposit failed!");
	    }
	}

	private void withdraw(HttpServletRequest request, HttpServletResponse response)
	        throws SQLException, ClassNotFoundException, IOException, AccountNumberNotExistException {

	    String accountNo = request.getParameter("accountNo");
	    HttpSession session = request.getSession(false);
	    Integer customerId = null;

	    if (session != null && session.getAttribute("customer_id") != null) {
	        customerId = Integer.parseInt(session.getAttribute("customer_id").toString());
	    }

	    double amount = Double.parseDouble(request.getParameter("amount"));
	    boolean success = false;

	    if (accountNo == null && customerId != null) {
	        try {
				success = transactionService.withdraw(customerId, amount);
			} catch (ClassNotFoundException | SQLException | AccountNumberNotExistException
					| BalanceInsufficientExeception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } else if (accountNo != null && customerId == null) {
	        success = transactionService.withdraw(accountNo, amount);
	    } else {
	        response.getWriter().println("Invalid withdrawal request!");
	        return;
	    }

	    if (success) {
	        response.getWriter().println("Withdrawal successful!");
	    } else {
	        response.getWriter().println("Withdrawal failed!");
	    }
	}


	private void transaction(HttpServletRequest request, HttpServletResponse response)
	        throws SQLException, ClassNotFoundException, IOException, AccountNumberNotExistException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    
	    StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Step 2: Convert JSON string to a Java object
        String jsonString = jsonBuffer.toString();
        
        
	    
	    HttpSession session = request.getSession(false);
	    Integer customerId = session != null ? (Integer.parseInt(session.getAttribute("customer_id").toString())): null;


        TransactionRequest transactionRequest = objectMapper.readValue(jsonString, TransactionRequest.class);

        // Extract data from the request
        String method = transactionRequest.getMethod();
        double amount = transactionRequest.getAmount();
        String mobileNo = transactionRequest.getMobileNo();
//        String accountNo = transactionRequest.getAccountNo();
        String receiverAccountNo = transactionRequest.getReceiverAccountNo();
//        String password = transactionRequest.getPassword();
	    boolean success = false;
	    String message = "";
	    
//	    if(savingsAccountService.isValidUser(customerId, password)) {
	    	
	    

	    if ("mobileNo".equals(method)) {
	        // Handle transaction via mobile number
	        if (customerId != null) {
	            try {
					success = transactionService.transactionToReceiverAccountNoByCustomerId(customerId, mobileNo, amount);
		            message = success ? "Transaction successful!" : "Transaction failed!";

				} catch (ClassNotFoundException | SQLException | AccountNumberNotExistException
						| BalanceInsufficientExeception e) {
					// TODO Auto-generated catch block
					message = e.getMessage();
					e.printStackTrace();
				}
	        } else {
	            message = "Customer ID not found!";
	        }
	    } else if ("accountNo".equals(method)) {
	        // Handle transaction via account number
	        if (receiverAccountNo != null) {
	            try {
	            	
					success = transactionService.transactionToReceiverAccountNoByCustomerId(customerId, receiverAccountNo, amount);
		            message = success ? "Transaction successful!" : "Transaction failed!";

				} catch (ClassNotFoundException | SQLException | AccountNumberNotExistException
						| BalanceInsufficientExeception e) {
					
					message = e.getMessage();
					e.printStackTrace();
				}
           
	        } else {
	            message = "Account number is required!";
	        }
	    }
	    
//	    }
//	    else {
//	    	 message = "Invalid password!";
//	    }

        JSONObject json = new JSONObject();

	    // Build JSON response
//	    String jsonResponse = "{\"success\": " + success + ", \"message\": \"" + message + "\"}";
        
        json.append("success", success);
        json.append("message", message);
	    // Send JSON response
	    response.getWriter().write(json.toString());
	    
	}


}
