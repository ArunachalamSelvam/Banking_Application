package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.banking.entities.SavingsAccount;
import com.banking.exception.AccountNumberNotExistException;
import com.banking.requestEntities.SavingsAccountWithCustomer;
import com.banking.serviceImpl.SavingsAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SavingsAccountServlet
 */

public class SavingsAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final SavingsAccountServiceImpl savingsAccountService = SavingsAccountServiceImpl.getInstance();
	
    private final ObjectMapper objectMapper = new ObjectMapper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SavingsAccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
            return;
        }
        
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "/SavingsAccountServlet/getAll":
                    getAllSavingsAccounts(response);
                    break;
                case "/SavingsAccountServlet/getByAccountNo":
                    getSavingsAccountByAccountNo(request, response);
                    break;
                    
                case "getBalance":   
                	getBalanceByCustomerId(request, response);
                	break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + path);
                    break;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SavingsAccountWithCustomer savingsAccountWithCustomer = objectMapper.readValue(request.getInputStream(), SavingsAccountWithCustomer.class);
			
		//			response.getWriter().write(objectMapper.writeValueAsString(savingsAccount));
//		response.getWriter();
		
	}
	
	@Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
            return;
        }

        try {
            switch (path) {
                case "/update":
                    updateSavingsAccount(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + path);
                    break;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
            return;
        }

        try {
            switch (action) {
                case "delete":
                    deleteSavingsAccount(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
                    break;
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
        }
    }
	
	
	 private void getAllSavingsAccounts(HttpServletResponse response) throws IOException {
	        List<SavingsAccount> accounts = savingsAccountService.getAllSavingsAccounts();
	        // Convert to JSON and write to response
	        response.getWriter().write(accounts.toString()); // Simplified for example
	    }

	    private void getSavingsAccountByAccountNo(HttpServletRequest request, HttpServletResponse response) throws IOException, AccountNumberNotExistException {
	        String accountNo = request.getParameter("accountNo");
	        if (accountNo == null) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account number is missing");
	            return;
	        }

	        SavingsAccount account = savingsAccountService.getSavingsAccountByAccountNo(accountNo);
	        if (account != null) {
	            // Convert to JSON and write to response
	            response.getWriter().write(account.toString()); // Simplified for example
	        } else {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
	        }
	    }
	    
	    private void getBalanceByAccountNo(HttpServletRequest request, HttpServletResponse response) throws IOException, AccountNumberNotExistException {
	        String accountNo = request.getParameter("accountNo");
	        if (accountNo == null) {
	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account number is missing");
	            return;
	        }

	        SavingsAccount account = savingsAccountService.getSavingsAccountByAccountNo(accountNo);
	        if (account != null) {
	            // Convert to JSON and write to response
	            response.getWriter().write(account.toString()); // Simplified for example
	        } else {
	            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
	        
	        }
	    }
	
	    
	    private void getBalanceByCustomerId(HttpServletRequest request, HttpServletResponse response) throws IOException, AccountNumberNotExistException {
	    	HttpSession session = request.getSession(false);

	    	if (session != null) {
	    	    Object customerIdObj = session.getAttribute("customer_id");
	    	    if (customerIdObj != null) {
	    	        try {
	    	            Integer customerId = Integer.parseInt(customerIdObj.toString());
	    	            Double balance = savingsAccountService.getBalanceByCustomerId(customerId);
	    	            if(balance!=null) {
	    	            	String formattedBalance = String.format("%.2f", balance);
	                        
	                        // Create the JSON response
	                        JsonObject json = new JsonObject();
	                        json.addProperty("balance", formattedBalance);
	                        json.addProperty("success", true);
	    	            	response.getWriter().write(json.toString()); 
	    	            }
	    	            // Now you can safely use customerId
	    	        } catch (NumberFormatException e) {
	    	            // Handle the case where the customer_id is not a valid integer
	    	            e.printStackTrace();
	    	        }
	    	    } else {
	    	        // Handle the case where customer_id is not in the session
	    	    }
	    	} else {
	    	    // Handle the case where no session exists
	    	}

	       
	    }
	private void createSavingsAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        // Parse request to create SavingsAccountWithCustomer
        SavingsAccountWithCustomer savingsAccountWithCustomer = new SavingsAccountWithCustomer();
        // Set properties from request parameters
        // savingsAccountWithCustomer.set... 

//        SavingsAccount createdAccount = savingsAccountService.saveSavingsAccount(savingsAccountWithCustomer);
        // Convert to JSON and write to response
//        response.getWriter().write(createdAccount.toString()); // Simplified for example
    }

    private void updateSavingsAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        Integer accountId = Integer.parseInt(request.getParameter("accountId"));
        // Parse request to create SavingsAccountWithCustomer
        SavingsAccountWithCustomer savingsAccountWithCustomer = new SavingsAccountWithCustomer();
        // Set properties from request parameters
        // savingsAccountWithCustomer.set...

        SavingsAccount updatedAccount = savingsAccountService.updateSavingsAccount(accountId, savingsAccountWithCustomer);
        // Convert to JSON and write to response
        response.getWriter().write(updatedAccount.toString()); // Simplified for example
    }

    private void deleteSavingsAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, ClassNotFoundException {
        Integer accountId = Integer.parseInt(request.getParameter("accountId"));
        int result = savingsAccountService.deleteSavingsAccount(accountId);
        if (result > 0) {
            response.getWriter().write("Account deleted successfully");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
        }
    }

}
