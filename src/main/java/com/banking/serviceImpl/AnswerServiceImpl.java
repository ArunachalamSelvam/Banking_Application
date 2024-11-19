package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Answer;
import com.banking.service.AnswerService;

public class AnswerServiceImpl implements AnswerService{

	private static final DbManager DB_MANAGER = DbManager.getInstance();
	
	private static AnswerServiceImpl instance = null;
	private AnswerServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static AnswerServiceImpl getInstance() {
		if(instance == null) {
			instance = new AnswerServiceImpl();
		}
		return instance;
	}
	
	
	@Override
	public List<Answer> saveAnswer(List<Answer> answers) throws ClassNotFoundException, SQLException {
		System.out.println("Inside Answer service");
		List<Answer> result = new ArrayList<>(); 
		
		for(Answer answer : answers) {
			String query = "INSERT INTO \"bankSchema\".answer (question_id, customer_id, answer) VALUES (?,?,?)";
			try(Connection connection = DB_MANAGER.createConnection();
				 PreparedStatement pstmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
					pstmt.setInt(1, answer.getQuestionId());
					pstmt.setInt(2, answer.getCustomerId());
					pstmt.setString(3,answer.getAnswer());
					
					int affectedRows = pstmt.executeUpdate();

					if (affectedRows > 0) {
						try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								answer.setAnswerId(generatedKeys.getInt(1));
								result.add(answer);
							}
						}
					}	
			}
			
		}
		
		System.out.println("answerId"+result.get(0).getAnswerId());
		
		return result;
		
	}

	
	
}
