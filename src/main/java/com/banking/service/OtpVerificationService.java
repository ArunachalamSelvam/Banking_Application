package com.banking.service;

import java.sql.SQLException;

public interface OtpVerificationService {
	
	
//	public boolean updateOtp(String email, String otp);
//	boolean storeOtp(String email) throws ClassNotFoundException, SQLException;
	boolean deleteOtpData() throws SQLException, ClassNotFoundException;
	boolean storeOtp(String email, String otp) throws ClassNotFoundException, SQLException;
	boolean verifyOtp(String email, String otp) throws ClassNotFoundException, SQLException;

}
