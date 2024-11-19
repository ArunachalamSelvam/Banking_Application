package com.banking.serviceImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.hc.core5.http.ParseException;

import com.banking.db.DbManager;
import com.banking.exception.InvalidEmailException;
import com.banking.externalApiClasses.CliqApiManager;
import com.banking.service.OtpVerificationService;
import com.banking.utils.OtpGenerator;

public class OtpVerificationServiceImpl implements OtpVerificationService {
	
	private final DbManager DB_MANAGER = DbManager.getInstance();
	
	private final OtpGenerator OTP_GENERATOR = OtpGenerator.getInstance();
	
	private final CliqApiManager CLIQ_API = CliqApiManager.getInstance();
	
	private static OtpVerificationServiceImpl instance = null;
	
	public static OtpVerificationServiceImpl getInstance() {
		if(instance == null) {
			instance = new OtpVerificationServiceImpl();
		}
		
		return instance;
	}
	
	private OtpVerificationServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean sendOtp(String email) throws IOException, ClassNotFoundException, SQLException, InvalidEmailException, ParseException {
		
		String otp = OTP_GENERATOR.getOtp(6);

		
		String message = "This is your otp - " + otp +". It is valid for 1 minute.";
		boolean isMessageSend = CLIQ_API.sendMessageToZohoCliq(email, message);
		boolean isOtpStored = false;
		if(isMessageSend) {
			
			isOtpStored = storeOtp(email, otp);
			
		}
		return isOtpStored;
		
	}

	@Override
	public boolean storeOtp(String email,String otp) throws ClassNotFoundException, SQLException {
		
		String query = "Insert into \"bankSchema\".otp_verifications (email_id, otp,expires_at) values (?,?,NOW() + INTERVAL \'1 minute\')";
//		String otp = OTP_GENERATOR.getOtp(6);
	
		try (Connection connection = DB_MANAGER.createConnection();
	             PreparedStatement pstmt = connection.prepareStatement(query)) {

			pstmt.setString(1, email);
			pstmt.setString(2, otp);
			
			return pstmt.executeUpdate()>0;
			
		}
		
		
		
	}

	@Override
	public boolean deleteOtpData() throws SQLException, ClassNotFoundException {
		String query = "DELETE FROM \"bankSchema\".otp_verifications WHERE expires_at < NOW()";
		try (Connection connection = DB_MANAGER.createConnection();
	             PreparedStatement pstmt = connection.prepareStatement(query)) {

			
			return pstmt.executeUpdate()>0;
			
		}
	}

	@Override
	public boolean verifyOtp(String email, String otp) throws ClassNotFoundException, SQLException {
		String query = "SELECT * FROM \"bankSchema\".otp_verifications WHERE email_id = ? AND otp = ? AND expires_at > NOW()";
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = DB_MANAGER.createConnection();
			pstmt = connection.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, otp);
			
			try (ResultSet resultSet = pstmt.executeQuery()) {
                // Check if OTP exists and is still valid
                return resultSet.next(); // returns true if a record is found
            }
			
		}finally {
			connection.close();
			pstmt.close();
		}
	}

}
