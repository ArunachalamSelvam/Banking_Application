package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.entities.Answer;

public interface AnswerService {


	List<Answer> saveAnswer(List<Answer> answers) throws ClassNotFoundException, SQLException;
	
}
