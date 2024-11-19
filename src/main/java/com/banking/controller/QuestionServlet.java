package com.banking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.banking.entities.Questions;
import com.banking.serviceImpl.QuestionServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class QuestionServlet
 */
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final QuestionServiceImpl questionService = QuestionServiceImpl.getInstance();
	private static final ObjectMapper objectMapper= new ObjectMapper();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			
			
			List<Questions> questionList = questionService.getSecurityQuestions();
			
			// Convert the list to a JSON string
						String json = objectMapper.writeValueAsString(questionList);
						
						// Set the response content type to application/json
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						
						// Send the JSON string in the response
						response.getWriter().write(json);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");

		}
		
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		String question = request.getParameter("question");
		
		try {
		
		List<Map<String,String>> list = objectMapper.readValue(request.getInputStream(), List.class);
		
		 boolean isSaved = questionService.addQuestion(list);
		 
		 response.getWriter().write("Questions added successfully.");
		}
		catch(Exception e) {
			e.printStackTrace();
			
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
		}
		
		
		
		
	}

}
