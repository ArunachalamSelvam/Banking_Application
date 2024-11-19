package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.User;
import com.banking.service.UserService;

public class UserServiceImpl implements UserService {
	
	private static final DbManager DB_MANAGER = DbManager.getInstance();
	
	

	public UserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	 public User saveUser(User user) throws ClassNotFoundException, SQLException {
        String insertQuery = "INSERT INTO \"bankSchema\".user (first_name, last_name, mobile_number, email_id, password, active_status) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getMobileNumber());
            pstmt.setString(4, user.getEmailId());
            pstmt.setString(5, user.getPassword());
            pstmt.setBoolean(6, user.getActiveStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
            throw e; // Rethrow to propagate the exception
        }

        return user;
    }

	@Override
	public User updateUser(Integer userId, User user) {
	    String updateQuery = "UPDATE \"bankSchema\".user SET first_name = ?, last_name = ?, " +
	                         "mobile_number = ?, email_id = ?, password = ?, active_status = ? " +
	                         "WHERE user_id = ?";

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

	        pstmt.setString(1, user.getFirstName());
	        pstmt.setString(2, user.getLastName());
	        pstmt.setString(3, user.getMobileNumber());
	        pstmt.setString(4, user.getEmailId());
	        pstmt.setString(5, user.getPassword());
	        pstmt.setBoolean(6, user.getActiveStatus());
	        pstmt.setInt(7, userId);

	        int affectedRows = pstmt.executeUpdate();
	        if (affectedRows > 0) {
	            return user;
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return null; // Return null if update fails
	}


	@Override
	public List<User> getAllUsers() {
	    String selectQuery = "SELECT * FROM \"bankSchema\".user";
	    List<User> userList = new ArrayList<>();

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(selectQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            User user = new User();
	            user.setUserId(rs.getInt("user_id"));
	            user.setFirstName(rs.getString("first_name"));
	            user.setLastName(rs.getString("last_name"));
	            user.setMobileNumber(rs.getString("mobile_number"));
	            user.setEmailId(rs.getString("email_id"));
	            user.setPassword(rs.getString("password"));
	            user.setActiveStatus(rs.getBoolean("active_status"));

	            userList.add(user);
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return userList;
	}

	@Override
	public User getUserById(Integer userId) {
	    String selectQuery = "SELECT * FROM \"bankSchema\".user WHERE user_id = ?";
	    User user = null;

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

	        pstmt.setInt(1, userId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setUserId(rs.getInt("user_id"));
	                user.setFirstName(rs.getString("first_name"));
	                user.setLastName(rs.getString("last_name"));
	                user.setMobileNumber(rs.getString("mobile_number"));
	                user.setEmailId(rs.getString("email_id"));
	                user.setPassword(rs.getString("password"));
	                user.setActiveStatus(rs.getBoolean("active_status"));
	            }
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return user;
	}


	@Override
	public int deleteUser(Integer userId) {
	    String deleteQuery = "DELETE FROM \"bankSchema\".user WHERE user_id = ?";

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

	        pstmt.setInt(1, userId);

	        return pstmt.executeUpdate();

	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return 0; // Return 0 if deletion fails
	}


}
