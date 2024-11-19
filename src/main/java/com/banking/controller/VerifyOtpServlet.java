package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.JSONObject;

import com.banking.serviceImpl.OtpVerificationServiceImpl;

/**
 * Servlet implementation class VerifyOtpServlet
 */
public class VerifyOtpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final OtpVerificationServiceImpl otpApi = OtpVerificationServiceImpl.getInstance();

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifyOtpServlet() {
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
        String action1 = request.getParameter("action")==null ? "" : request.getParameter("action");

		if(action1.equals("verifyOtp")) {
//			String otp = request.getParameter("otp");
//			jsonBuffer = new StringBuilder();
			String email = request.getParameter("email");
		      String otp = request.getParameter("otp");
			
			try {
				boolean isVerified = otpApi.verifyOtp(email, otp);
				
		        
		        if (isVerified) {
//		            jsonResponse.put("redirect", true); 
		        	System.out.println("Send redirect before");
		        	
		        	HttpSession session = request.getSession();
		        	
		        	session.setAttribute("verifyEmail", email);

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(action1.equals("forgotVerifyOtp")) {
//			String otp = request.getParameter("otp");
//			jsonBuffer = new StringBuilder();
			String email = request.getParameter("email");
		    String otp = request.getParameter("otp");
			
			try {
				boolean isVerified = otpApi.verifyOtp(email, otp);
				
		        
		        if (isVerified) {
//		            jsonResponse.put("redirect", true); 
		        	System.out.println("Send redirect before");
		        	
		        	HttpSession session = request.getSession();
		        	
		        	session.setAttribute("verifyEmail", email);

		        	response.sendRedirect("passwordReset.html");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
