package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.banking.entities.State;
import com.banking.service.StateService;
import com.banking.serviceImpl.StateServiceImpl;
import com.google.gson.Gson;

/**
 * Servlet implementation class StateServlet
 */
@WebServlet("/stateServlet")
public class StateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private final StateService stateService = StateServiceImpl.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer countryId = Integer.parseInt(request.getParameter("countryId"));
		List<State> states = stateService.getStatesByCountryId(countryId);
        response.setContentType("application/json");
        response.getWriter().write(new Gson().toJson(states)); 

//		String path = request.getServletPath();
////		 String path = request.getServletPath();
//
//	        if (path == null) {
//	            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
//	            return;
//	        }
//
//	        try {
//	            switch (path) {
//	                case "/getAll":
//	                	  List<State> states = stateService.getAllStates();
//	                      response.setContentType("application/json");
//	                      response.getWriter().write(new Gson().toJson(states)); 
//	                      break;
//	                default:
//	                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + path);
//	                    break;
//	            }
//	        } catch (Exception e) {
//	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request: " + e.getMessage());
//	        }
//		List<State> states = stateService.getAllStates();
//		
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
