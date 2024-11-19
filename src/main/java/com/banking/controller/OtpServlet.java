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

import org.apache.hc.core5.http.ParseException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banking.exception.InvalidCustomerException;
import com.banking.exception.InvalidEmailException;
import com.banking.serviceImpl.CustomerServiceImpl;
import com.banking.serviceImpl.OtpVerificationServiceImpl;


import java.util.logging.Level;

/**
 * Servlet implementation class OtpServlet
 */
public class OtpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final OtpVerificationServiceImpl otpApi = OtpVerificationServiceImpl.getInstance();
	
	private final CustomerServiceImpl customerService = CustomerServiceImpl.getInstance();

	private static Logger LOGGER = LoggerFactory.getLogger(OtpServlet.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OtpServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("Inside Otp Servlet");
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
		String action = jsonObject.getString("action");
		String email = jsonObject.getString("email");
		String otp = jsonObject.getString("otp");

		if (action == null || email == null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"response\":\"Invalid action or missing parameters.\"}");
			return;
		}

		if (action.equals("sendOtp")) {
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				boolean isOtpSend = otpApi.sendOtp(email);
				if (isOtpSend) {
//				response.getWriter().write("{\"response\":\"Message sent successfully.\"}");
//				HttpSession session = request.getSession();
//				session.setAttribute("email", email);
//				response.sendRedirect("verifyOtp.jsp");

					response.getWriter().write("{\"response\":\"otp sent successfully.\"}");
				} else {

					response.getWriter().write(
							"{\"response\":\"Sorry, we couldn't process your request due to a technical error. Please try again later.\",\"code\":\"operation_failed\"}\n"
									+ "\"}");
				}
			} catch (ParseException | ClassNotFoundException | IOException | SQLException e) {
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
				e.printStackTrace();
			} catch (InvalidEmailException e) {
				response.getWriter().write("{\"error\":\"invalid email.\"}");
				e.printStackTrace();
			} catch (Exception e) {
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
			}
		}

		if (action.equals("sendOtpFirst")) {
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				boolean isOtpSend = otpApi.sendOtp(email);
				if (isOtpSend) {
//					response.getWriter().write("{\"response\":\"Message sent successfully.\"}");
					System.out.println("inside first otp ");
					HttpSession session = request.getSession();
					session.setAttribute("email", email);
					response.setContentType("application/json");
					response.getWriter().write("{\"redirect\":\"verifyOtp.jsp\"}");

				} else {

					response.getWriter().write(
							"{\"response\":\"Sorry, we couldn't process your request due to a technical error. Please try again later.\",\"code\":\"operation_failed\"}\n"
									+ "\"}");
				}
			} catch (ParseException | ClassNotFoundException | IOException | SQLException e) {

				response.getWriter().write(
						"{\"response\":\"Sorry, we couldn't process your request due to a technical error. Please try again later.\",\"code\":\"operation_failed\"}\n"
								+ "\"}");
				e.printStackTrace();
			} catch (InvalidEmailException e) {
				LOGGER.error(e.getMessage());
				response.getWriter().write("{\"error\":\"invalid email.\"}");
				e.printStackTrace();
			} catch (Exception e) {
				LOGGER.error("An unexpected error occurred", e);
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
				e.printStackTrace();
			}
		}
		else if (action.equals("sendOtpForgotPassword")) {
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				
				boolean  isValidCustomer = customerService.isValidCustomer(email);
				
				if(isValidCustomer) {
				boolean isOtpSend = otpApi.sendOtp(email);
				if (isOtpSend) {
//					response.getWriter().write("{\"response\":\"Message sent successfully.\"}");
					System.out.println("inside first otp ");
					HttpSession session = request.getSession();
					session.setAttribute("email", email);
					response.setContentType("application/json");
					response.getWriter().write("{\"redirect\":\"forgotPasswordVerify.jsp\"}");

				} else {

					response.getWriter().write(
							"{\"response\":\"Sorry, we couldn't process your request due to a technical error. Please try again later.\",\"code\":\"operation_failed\"}\n"
									+ "\"}");
				}
				}else {
					throw new InvalidCustomerException("Customer is not valid");
				}
				
			}catch( InvalidCustomerException e) {
				response.getWriter().write("{\"error\" : \"UserName not found.\"}");
				e.printStackTrace();
			}
			
			
			catch (ParseException | ClassNotFoundException | IOException | SQLException e) {
				

				response.getWriter().write(
						"{\"response\":\"Sorry, we couldn't process your request due to a technical error. Please try again later.\",\"code\":\"operation_failed\"}\n"
								+ "\"}");
				e.printStackTrace();
			} catch (InvalidEmailException e) {
				LOGGER.error(e.getMessage());
				response.getWriter().write("{\"error\":\"invalid email.\"}");
				e.printStackTrace();
			} catch (Exception e) {
				LOGGER.error("An unexpected error occurred", e);
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
				e.printStackTrace();
			}
		}


		else if (action.equals("delete")) {
			try {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				System.out.println("Inside delete ");
				boolean isDeleted = otpApi.deleteOtpData();
				if (isDeleted) {
					System.out.println("deleted");

					response.getWriter().write("{\"response\":\"deleted\"}");
				} else {
					System.out.println("not deleted ");

					response.getWriter().write("{\"response\":\"not deleted\"}");
				}
			} catch (ClassNotFoundException | SQLException e) {
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
			}
		}

		else if (action.equals("verifyOtp")) {
//			String otp = request.getParameter("otp");
//			jsonBuffer = new StringBuilder();
			email = request.getParameter("email");
			otp = request.getParameter("otp");

			try {
				boolean isVerified = otpApi.verifyOtp(email, otp);

				if (isVerified) {
//		            jsonResponse.put("redirect", true); 
//		        	System.out.println("Send redirect before");

					response.sendRedirect("signup.html");
					System.out.println("Send redirect after");
				} else {
					JSONObject jsonResponse = new JSONObject();
					jsonResponse.put("response", "Invalid OTP or OTP expired");
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					PrintWriter out = response.getWriter();
					out.print(jsonResponse.toString());
					out.flush();
				}

			} catch (ClassNotFoundException | SQLException e) {
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
				e.printStackTrace();
			} catch (Exception e) {
				response.getWriter().write("{\"response\":\"some error occurs try again later.\"}");
			}
		}
	}

}
