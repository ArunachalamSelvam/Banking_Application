package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.TransactionType;
import com.banking.service.TransactionTypeService;

public class TransactionTypeServiceImpl implements TransactionTypeService {
	
    private final DbManager DB_MANAGER = DbManager.getInstance();

    @Override
    public List<TransactionType> getAllTransactionTypes() {
        List<TransactionType> transactionTypes = new ArrayList<>();
        try (Connection conn =DB_MANAGER.createConnection()) {
            String query = "SELECT * FROM \"bankSchema\".transaction_type";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TransactionType transactionType = new TransactionType();
                transactionType.setTransactionTypeId(rs.getInt("transaction_type_id"));
                transactionType.setTransactionType(rs.getString("transaction_type"));
                transactionTypes.add(transactionType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionTypes;
    }
    
    

    @Override
    public boolean addTransactionType(TransactionType transactionType) {
        boolean isAdded = false;
        try (Connection conn = DB_MANAGER.createConnection()) {
            String query = "INSERT INTO \"bankSchema\".transaction_type (transactionType) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, transactionType.getTransactionType());
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAdded;
    }


}
