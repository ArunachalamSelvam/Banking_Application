package com.banking.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONObject;

import com.banking.requestEntities.LoginRequest;
import com.banking.requestEntities.SignUpData;
import com.banking.responseEntities.LoginResponse;
import com.banking.service.CustomerService;
import com.banking.service.EmployeeService;
import com.banking.serviceImpl.CustomerServiceImpl;
import com.banking.serviceImpl.EmployeeServiceImpl;
import com.banking.serviceImpl.LoginServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class LoginServlet
 */

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final LoginServiceImpl loginService = LoginServiceImpl.getInstance();
	private final CustomerService customerService = CustomerServiceImpl.getInstance();
	
	private final EmployeeService employeeService = EmployeeServiceImpl.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String action = request.getParameter("action");
//
//        if ("login".equals(action)) {
//            String username = request.getParameter("username");
//            String password = request.getParameter("password");
//            String ipAddress = request.getRemoteAddr();
//
//            LoginResponse token = loginService.login(username, password,ipAddress);
//
//            response.setContentType("application/json");
//            PrintWriter out = response.getWriter();
//            if (token != null) {
//    			response.getWriter().write(objectMapper.writeValueAsString(token));
//                out.write("{\"token\":\"" + token + "\"}");
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                out.write("{\"error\":\"Invalid credentials\"}");
//            }
//        } else if ("validate".equals(action)) {
//            String token = request.getHeader("Authorization").replace("Bearer ", "");
//            boolean isValid = loginService.validateToken(token);
//
//            response.setContentType("application/json");
//            PrintWriter out = response.getWriter();
//            if (isValid) {
//                out.write("{\"status\":\"valid\"}");
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                out.write("{\"status\":\"invalid\"}");
//            }
//        }
//    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		String userName = request.getParameter("username");
//        String password = request.getParameter("password");

        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Step 2: Convert JSON string to a Java object
        String jsonString = jsonBuffer.toString();
        
        LoginRequest requestData =  objectMapper.readValue(jsonString, LoginRequest.class);
        
        String userName = requestData.getUsername();
        String password = requestData.getPassword();
        
        
        try {
        	
        	  HttpSession session = request.getSession(false); // Check if a session already exists
              if (session != null) {
                  session.invalidate(); // Invalidate the existing session if present
                  System.out.println("Existing session invalidated: " + session.getId());
              }
              session = request.getSession(); // Create a new session
              System.out.println("New session created: " + session.getId());


        	if(customerService.isCustomerExists(userName, password)) {
        		LoginResponse customer = customerService.authenticateCustomer(userName, password);
//        		session = request.getSession();
            	session.setAttribute("customer_id", customer.getId());
//            	session.setAttribute("isAtmCardAvailable", customer.getIsAtmCardAdded());
//            	session.setMaxInactiveInterval(1 * 60);
            	
            	Cookie cookie = new Cookie("userName", userName);
            	cookie.setMaxAge(30);
            	 JSONObject json = new JSONObject();
                 json.append("userName", customer.getName());
                 json.append("success", true);
                 json.append("message", "Login successful!");
                 System.out.println("inside customerlogin");

             	response.getWriter().write(json.toString());	
//            	response.getWriter().write(customer.getName());	 
        	}
        	
        	else if(employeeService.isEmployeeExistsByUserNameANdPassword(userName, password)) {
        		LoginResponse employee = employeeService.authenticateEmployee(userName, password);
//        		session = request.getSession();
            	session.setAttribute("employee_id", employee.getId());
            	session.setMaxInactiveInterval(1 * 60);
            	
                JSONObject json = new JSONObject();
                json.append("name", employee.getName());

            	response.sendRedirect("adminDashboard.html");	 
        	}
        	
           else {
                // Invalid credentials
        	   System.out.println("invalid login");
        	   JSONObject json = new JSONObject();
//               json.append("userName", customer.getName());
//               json.append("success", false);
               json.append("message", "invalid username / password");
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(json.toString());
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred while processing your request.");
            e.printStackTrace();
        }

       
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		
	}
	

}
