package com.banking.entities;

public class Answer {
	
	private int answerId;
	private int questionId;
	private int customerId;
	private String answer;
	
	
	public Answer() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Answer(int questionId, int customerId, String answer) {
		super();
		this.questionId = questionId;
		this.customerId = customerId;
		this.answer = answer;
	}



	public int getAnswerId() {
		return answerId;
	}
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
}
