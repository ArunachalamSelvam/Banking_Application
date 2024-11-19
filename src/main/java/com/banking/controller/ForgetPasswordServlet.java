package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.hc.core5.http.ParseException;

import com.banking.exception.InvalidEmailException;
import com.banking.externalApiClasses.CliqApiManager;
import com.banking.serviceImpl.CustomerServiceImpl;

/**
 * Servlet implementation class ForgetPasswordServlet
 */

@WebServlet("/ForgetPasswordServlet")
public class ForgetPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final CustomerServiceImpl customerService = CustomerServiceImpl.getInstance();
	
	private final CliqApiManager cliqApi = CliqApiManager.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgetPasswordServlet() {
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
		
		HttpSession session = request.getSession(false);
		String email = (String)session.getAttribute("verifyEmail");
//		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		
		try {
			boolean isReset = customerService.resetPassword(email, newPassword);
			
			if(isReset) {
				String message = "Password Change successFully.";
				cliqApi.sendMessageToZohoCliq(email, message);
//				request.setAttribute("message", "Password changed successfully");
				request.getSession().setAttribute("message", "Password changed successfully");
				response.sendRedirect("success.jsp");
			}
			
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().write("{\"Error\" : \"Password reset failed.\"}");

			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
