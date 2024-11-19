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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

import org.apache.hc.core5.http.ParseException;

import com.banking.entities.Address;
import com.banking.entities.Answer;
import com.banking.entities.Customer;
import com.banking.entities.SavingsAccount;
import com.banking.exception.InvalidEmailException;
import com.banking.externalApiClasses.CliqApiManager;
import com.banking.requestEntities.SavingsAccountWithCustomer;
import com.banking.requestEntities.SignUpData;
import com.banking.service.SavingsAccountService;
import com.banking.serviceImpl.SavingsAccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class SignUpServlet
 */

@WebServlet("/signUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private SavingsAccountService savingsAccountService = SavingsAccountServiceImpl.getInstance(); 
    
    private CliqApiManager cliqApi = CliqApiManager.getInstance();
    
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
	
		
//		Enumeration<String> parameterNames = request.getParameterNames();
//		while (parameterNames.hasMoreElements()) {
//		    String paramName = parameterNames.nextElement();
//		    System.out.println(paramName + ": " + request.getParameter(paramName));
//		}
		
		StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Step 2: Convert JSON string to a Java object
        String jsonString = jsonBuffer.toString();
//        ObjectMapper objectMapper = new ObjectMapper();
        SignUpData signUpData = objectMapper.readValue(jsonString, SignUpData.class);

		
		
        // Retrieve and process form data
//        String firstName = request.getParameter("firstName");
//        System.out.println(firstName);
//        String middleName = request.getParameter("middleName");
//        System.out.println(middleName);
//        String lastName = request.getParameter("lastName");
//        System.out.println(lastName);
//        String fatherName = request.getParameter("fatherName");
//        System.out.println(fatherName);
//        String motherName = request.getParameter("motherName");
//        System.out.println(motherName);
//        String gender = request.getParameter("gender");
//        System.out.println(gender);
//        String mobileNumber = request.getParameter("mobileNumber");
//        System.out.println(mobileNumber);
//        String emailId = request.getParameter("emailId");
//        System.out.println(emailId);
//        String adharNumber = request.getParameter("adharNumber");
//        System.out.println(adharNumber);
//        String panNumber = request.getParameter("panNumber");
//        System.out.println(panNumber);
//        String password = request.getParameter("password");
//        System.out.println(password);
//        String address = request.getParameter("address");
//        System.out.println(address);
//        Integer stateId = Integer.parseInt(request.getParameter("stateId"));
//        System.out.println("stateId : " + stateId);
//        Integer countryId = Integer.parseInt(request.getParameter("countryId"));
//        System.out.println("CountryId : " + countryId);
////        Boolean activeStatus = Boolean.parseBoolean(request.getParameter("activeStatus"));
//        Integer branchId = Integer.parseInt(request.getParameter("branchId"));
//        System.out.println("Branch Id : " + branchId);
//        Boolean atmCardNeeded = Boolean.parseBoolean(request.getParameter("atmCardNeeded"));
//        System.out.println("atmCardNeeded : " + atmCardNeeded);
        
        HttpSession session = request.getSession(false);
        String emailId = null;
        try {
        if(session != null) {
        
         emailId = session.getAttribute("verifyEmail").toString();
         
        }else {
        	throw new InvalidEmailException("Session might ends or invalid email");
        }
        }catch (InvalidEmailException e) {
        	JsonObject errorMessage = new JsonObject();
			errorMessage.addProperty("error", true);
			errorMessage.addProperty("message", "Invalid Email");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write(errorMessage.toString());
			e.printStackTrace();
		}

        String base64Image = signUpData.getPhotoData();
        System.out.println();
        System.out.println(base64Image);
        // Create Address object
        Address addr = new Address();
        addr.setAddress(signUpData.getAddress());
        addr.setStateId(signUpData.getStateId());
        addr.setCountryId(signUpData.getCountryId());
        addr.setActiveStatus(true);
        
        // Create Customer object
        Customer customer = new Customer();
        customer.setFirstName(signUpData.getFirstName());
        customer.setMiddleName(signUpData.getMiddleName());
        customer.setLastName(signUpData.getLastName());
        customer.setFatherName(signUpData.getFatherName());
        customer.setMotherName(signUpData.getMotherName());
        customer.setGender(signUpData.getGender());
        customer.setMobileNumber(signUpData.getMobileNumber());
        customer.setEmailId(emailId);
        customer.setAdharNumber(signUpData.getAdharNumber());
        customer.setPanNumber(signUpData.getPanNumber());
        customer.setPassword(signUpData.getPassword());
        customer.setRoleId(2);

        // Create SavingsAccount object
        SavingsAccount savingsAccount = new SavingsAccount();
//        savingsAccount.setMinBalance(5000.0); // Example minimum balance
//        savingsAccount.setInterestRate(4.0); // Example interest rate
        savingsAccount.setBranchId(signUpData.getBranchId());
        
//        List<Answer> answers = new ArrayList<>();
//        
//        answers.add(new Answer(signUpData.getQuestion1Id(), 0, signUpData.getAnswer1()));
//        answers.add(new Answer(signUpData.getQuestion2Id(), 0, signUpData.getAnswer2()));

       
        // Wrap them in SavingsAccountWithCustomer
        SavingsAccountWithCustomer savingsAccountWithCustomer = new SavingsAccountWithCustomer();
        savingsAccountWithCustomer.setCustomer(customer);
        savingsAccountWithCustomer.setAddress(addr);
        savingsAccountWithCustomer.setSavingsAccount(savingsAccount);
        savingsAccountWithCustomer.setAtmCardNeeded(signUpData.getIsAtmCardNeeded());
        savingsAccountWithCustomer.setAtmPin(signUpData.getAtmPin());
//        savingsAccountWithCustomer.setAnswers(answers);
        
        
        

        try {
            // Save the savings account with customer details
//            SavingsAccount savedSavingsAccount = savingsAccountService.saveSavingsAccount(savingsAccountWithCustomer);
            
            String userMessage = savingsAccountService.saveSavingsAccount(savingsAccountWithCustomer);
            HttpSession newSession = request.getSession();
            newSession.setAttribute("userName", customer.getEmailId());
//            session.setAttribute("atmPin", atmPin);
            
            if (base64Image != null && !base64Image.isEmpty()) {
                try {
                    // Decode the base64 string to a byte array
                    String[] parts = base64Image.split(",");
                    byte[] imageBytes = Base64.getDecoder().decode(parts[1]);

                    // Set the image name as emailId.png or any desired extension
                    String targetDirectory = "/Users/arun-21785/eclipse-workspace/Banking_Project/src/main/webapp/customerImages";
                    
                    Path imagePath = Paths.get(targetDirectory, emailId + ".png");
                    // Write the image to the specified path
                    Files.write(imagePath, imageBytes);

                    // Check if the image is successfully stored
                    if (Files.exists(imagePath)) {
                        System.out.println("Image successfully saved at: " + imagePath.toString());
                    } else {
                        System.out.println("Failed to save the image.");
                    }
                } catch (IOException e) {
                    System.out.println("Error saving image: " + e.getMessage());
                }
            } else {
                System.out.println("No image data provided.");
            }

            // Redirect to success page or show confirmation
//           JSONObject json = new JSONObject();
//           json.append("data", "Account Created");
//            response.getWriter().append(json.toString());
            
//            String message = "Hi! " + customer.getFirstName() +".\nYou have successfully created your bank account  ";
            
            try {
            	System.out.println(customer.getEmailId());
				boolean cliqResponse = cliqApi.sendMessageToZohoCliq(customer.getEmailId().trim(), userMessage);
//				HttpSession newSession = request.getSession(false);
				
				request.getSession().setAttribute("message", "Account created successfully");
				
				response.setContentType("application/json");
				response.getWriter().write("{\"message\": \"message sends successfully\"}");

			} catch (ParseException | IOException e) {
				e.printStackTrace();
			} catch (InvalidEmailException e) {
				JsonObject errorMessage = new JsonObject();
				errorMessage.addProperty("error", true);
				errorMessage.addProperty("message", "Invalid Email");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(errorMessage.toString());
				e.printStackTrace();
			}
            
//            request.setAttribute("atmPin", atmPin);
            
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/AtmPinInfo.jsp");
//            dispatcher.forward(request, response);
//            System.out.println("response forwarded");
//            response.sendRedirect("success.jsp");
        }catch (SQLException | ClassNotFoundException e) {
            // Handle exceptions and show an error page
            e.printStackTrace();
//            response.sendRedirect("error.jsp");
        }
    }

}
