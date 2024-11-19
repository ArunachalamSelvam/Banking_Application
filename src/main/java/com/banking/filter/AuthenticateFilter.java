package com.banking.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AuthenticateFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Inside filter");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false); // Check if a session already exists

		if (session == null || session.getAttribute("customerId") !=null) {
		
//			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not authenticated");
			

			httpResponse.sendRedirect(httpRequest.getContextPath() + "/sessionExpired.html");
			
			return;
//			RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/Banking_Project/login.html");
//            dispatcher.forward(httpRequest, httpResponse);
            
           

		}

        chain.doFilter(request, response);

	}

}
