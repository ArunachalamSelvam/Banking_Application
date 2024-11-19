package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.banking.entities.Country;
import com.banking.entities.State;
import com.banking.service.CountryService;
import com.banking.serviceImpl.CountryServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class CountryServlet
 */
@WebServlet("/countryServlet")
public class CountryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final CountryService countryService = CountryServiceImpl.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CountryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Country> countries = countryService.getAllCountry();
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(countries)); 

		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
