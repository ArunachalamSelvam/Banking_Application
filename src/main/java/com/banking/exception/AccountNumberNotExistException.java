package com.banking.exception;

public class AccountNumberNotExistException extends Exception {
	
	private String message;
	
	public AccountNumberNotExistException(String message) {
		super(message);
	}

}
