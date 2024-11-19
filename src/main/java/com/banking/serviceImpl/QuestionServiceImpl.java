package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.banking.db.DbManager;
import com.banking.entities.Questions;

public class QuestionServiceImpl {
	
	private static final DbManager DB_MANAGER = DbManager.getInstance();
	
	private static QuestionServiceImpl instance = null;
	
	private QuestionServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static QuestionServiceImpl getInstance() {
		if(instance == null) {
			instance = new QuestionServiceImpl();
		}
		return instance;
	}
	
	
	public List<Questions> getSecurityQuestions() throws SQLException, ClassNotFoundException{
		
		List<Questions> questionList = new ArrayList<>();
		String query = "SELECT * FROM \"bankSchema\".questions";
		
		try(Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)){
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int questionId = rs.getInt("question_id");
				String question = rs.getString("question");
				
				questionList.add(new Questions(questionId, question));
			}
			
		}
		
		return questionList;
		
	}

	public boolean addQuestion(List<Map<String,String>> questions) throws SQLException, ClassNotFoundException {
		
		String query = "Insert INTO \"bankSchema\".questions (question) VALUES (?)";
		
		try(Connection connection = DB_MANAGER.createConnection();
				PreparedStatement pstmt = connection.prepareStatement(query)){
			
			for(Map<String,String> map : questions) {
				pstmt.setString(1, map.get("question"));
				pstmt.addBatch();
			}
			
			int[] arr =  pstmt.executeBatch();
			
			if(arr.length>0) {
				return true;
			}
		}
		
		
		
		return false;
	}
}
