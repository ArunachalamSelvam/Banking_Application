package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Employee;
import com.banking.entities.LoginAttempt;
import com.banking.exception.RateLimitExceededException;
import com.banking.requestEntities.LoginRequest;
import com.banking.utils.BcryptUtil;


public class LoginServiceImpl {
	
	private static LoginServiceImpl instance = null;
	
	private final DbManager DB_MANAGER = DbManager.getInstance();
	
	private final BcryptUtil bcryptUtil = BcryptUtil.getInstance();
	
	private LoginServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static LoginServiceImpl getInstance() {
		if(instance==null) {
			instance = new LoginServiceImpl();
		}
		
		return instance;
	}
	
//	public LoginResponse login(String username, String password, String ipAddress) {
//		
//		if (isRateLimitExceeded(username, ipAddress)) {
//	        throw new RateLimitExceededException("Too many login attempts. Please try again later.");
//	    }
//		
//        String query = "SELECT * FROM \"bankSchema\".employee WHERE username = ?";
//        Connection connection = null;
//        LoginResponse response =  null;
//
//        try {
//            connection = DB_MANAGER.createConnection();
//            PreparedStatement pstmt = connection.prepareStatement(query);
//            pstmt.setString(1, username);
//            
//
//            ResultSet rs = pstmt.executeQuery();
//            
//            
//
//            if (rs.next()) {
//            	String hashedPassword = rs.getString("password");
//            	if(bcryptUtil.checkPassword(password, hashedPassword)) {
//                response = new LoginResponse();
//                response.setEmployeeId(rs.getInt("employee_id"));
//                response.setFirstName(rs.getString("first_name"));
//                response.setLastName(rs.getString("last_name"));
//                response.setEmailId(rs.getString("email_id"));
//                response.setActiveStatus(rs.getBoolean("active_status"));
//                response.setBranchId(rs.getInt("role_id"));
//                response.setMiddleName(rs.getString("middle_name"));
//                response.setToken(JwtUtil.generateToken(username));
//                
//                recordLoginAttempt(username, ipAddress);
//                
//                return response;
//            	}
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } 
//
//        return null;
//    }

    
//    public boolean validateToken(String token) {
//        try {
//            JwtUtil.validateToken(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
    
    public List<LoginAttempt> getLoginAttempts(String username, String ipAddress) {
        List<LoginAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM login_attempts WHERE username = ? AND ip_address = ?";
        
        try (Connection connection = DB_MANAGER.createConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, ipAddress);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String user = rs.getString("username");
                    String ip = rs.getString("ip_address");
                    Date attemptTime = rs.getTimestamp("attempt_time");

                    LoginAttempt attempt = new LoginAttempt(user, ip, attemptTime);
                    attempts.add(attempt);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return attempts;
    }
    
    public void recordLoginAttempt(String username, String ipAddress) {
        // Store the login attempt in the database or Redis
        try (Connection connection = DB_MANAGER.createConnection();
             PreparedStatement pstmt = connection.prepareStatement("INSERT INTO login_attempts (username, ip_address) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, ipAddress);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    
    public boolean isRateLimitExceeded(String username, String ipAddress) {
        // Define the rate limit parameters
        int maxAttempts = 10;
        long timeWindowMillis = 3600000; // 1 hour

        // Get the current time
        long now = System.currentTimeMillis();

        // Fetch recent login attempts from the database or Redis
        List<LoginAttempt> attempts = getLoginAttempts(username, ipAddress);

        // Filter attempts within the time window
        long recentAttempts = attempts.stream()
                                       .filter(attempt -> now - attempt.getAttemptTime().getTime() <= timeWindowMillis)
                                       .count();

        return recentAttempts >= maxAttempts;
    }
    


}
