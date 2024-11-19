package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.hc.core5.http.ParseException;

import com.banking.externalApiClasses.CliqApiManager;

/**
 * Servlet implementation class CliqApiTestServlet
 */
public class CliqApiTestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final CliqApiManager cliqApi = CliqApiManager.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CliqApiTestServlet() {
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
		String userMail = request.getParameter("userMail");
		String message = "Nandrigal \uD83D\uDE4F";
		
		try {
			String accessToken = cliqApi.getAccessToken();
			String responses = cliqApi.sendMessageToZohoCliq(accessToken, userMail, message);
			
			response.getWriter().write(responses);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
