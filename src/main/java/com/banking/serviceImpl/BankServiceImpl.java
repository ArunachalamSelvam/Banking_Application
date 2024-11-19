package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Bank;
import com.banking.service.BankService;

public class BankServiceImpl implements BankService {

	private final DbManager DB_MANAGER = DbManager.getInstance();
	
public static BankServiceImpl instance = null;
    
    private BankServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static BankServiceImpl getInstance() {
    	if(instance == null) {
    		instance = new BankServiceImpl();
    	}
    	
    	return instance;
    }
	
	@Override
	public Bank saveBank(Bank bank) {
	    String insertQuery = "INSERT INTO \"bankSchema\".bank (bank_name) VALUES (?)";

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

	        pstmt.setString(1, bank.getBankName());

	        int affectedRows = pstmt.executeUpdate();

	        if (affectedRows > 0) {
	            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    bank.setBankId(generatedKeys.getInt(1));
	                }
	            }
	        }

	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return bank;
	}


	@Override
	public Bank updateBank(Integer bankId, Bank bank) {
	    String updateQuery = "UPDATE \"bankSchema\".bank SET bank_name = ? WHERE bank_id = ?";

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

	        pstmt.setString(1, bank.getBankName());
	        pstmt.setInt(2, bankId);

	        int affectedRows = pstmt.executeUpdate();

	        if (affectedRows > 0) {
	            bank.setBankId(bankId);
	            return bank;
	        }

	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return null; // Return null if update fails
	}


	@Override
	public List<Bank> getAllBanks() {
	    String selectQuery = "SELECT * FROM \"bankSchema\".bank";
	    List<Bank> bankList = new ArrayList<>();

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(selectQuery);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            Bank bank = new Bank();
	            bank.setBankId(rs.getInt("bank_id"));
	            bank.setBankName(rs.getString("bank_name"));

	            bankList.add(bank);
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return bankList;
	}


	@Override
	public Bank getBankById(Integer bankId) {
	    String selectQuery = "SELECT * FROM \"bankSchema\".bank WHERE bank_id = ?";
	    Bank bank = null;

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

	        pstmt.setInt(1, bankId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                bank = new Bank();
	                bank.setBankId(rs.getInt("bank_id"));
	                bank.setBankName(rs.getString("bank_name"));
	            }
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return bank;
	}


	@Override
	public int deleteBank(Integer bankId) {
	    String deleteQuery = "DELETE FROM \"bankSchema\".bank WHERE bank_id = ?";

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

	        pstmt.setInt(1, bankId);

	        return pstmt.executeUpdate();

	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

	    return 0; // Return 0 if deletion fails
	}

}
