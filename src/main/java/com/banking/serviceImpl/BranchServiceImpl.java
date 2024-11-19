package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Branch;
import com.banking.service.BranchService;

public class BranchServiceImpl implements BranchService {

    private final DbManager DB_MANAGER = DbManager.getInstance();
    
public static BranchServiceImpl instance = null;
    
    private BranchServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	
	public static BranchServiceImpl getInstance() {
    	if(instance == null) {
    		instance = new BranchServiceImpl();
    	}
    	
    	return instance;
    }

    @Override
    public Branch saveBranch(Branch branch){
        String insertQuery = "INSERT INTO \"bankSchema\".branch (branch_name, branch_address_id, bank_id, ifsc_code) VALUES (?, ?, ?, ?)";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, branch.getBranchName());
            pstmt.setInt(2, branch.getBranchAddressId());
            pstmt.setInt(3, branch.getBankId());
            pstmt.setString(4, branch.getIfscCode());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        branch.setBranchId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

        return branch;
    }

    @Override
    public Branch updateBranch(Integer branchId, Branch branch) {
        String updateQuery = "UPDATE \"bankSchema\".branch SET branch_name = ?, branch_address_id = ?, bank_id = ?, ifsc_code = ? WHERE branch_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

            pstmt.setString(1, branch.getBranchName());
            pstmt.setInt(2, branch.getBranchAddressId());
            pstmt.setInt(3, branch.getBankId());
            pstmt.setString(4, branch.getIfscCode());
            pstmt.setInt(5, branchId);

            pstmt.executeUpdate();

        }catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

        return branch;
    }

    @Override
    public List<Branch> getAllBranches() {
        List<Branch> branches = new ArrayList<>();
        String selectQuery = "SELECT * FROM \"bankSchema\".branch";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Branch branch = new Branch();
                branch.setBranchId(rs.getInt("branch_id"));
                branch.setBranchName(rs.getString("branch_name"));
                branch.setBranchAddressId(rs.getInt("branch_address_id"));
                branch.setBankId(rs.getInt("bank_id"));
                branch.setIfscCode(rs.getString("ifsc_code"));
                branches.add(branch);
            }

        }catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

        return branches;
    }

    @Override
    public Branch getBranchById(Integer branchId) {
        Branch branch = null;
        String selectQuery = "SELECT * FROM \"bankSchema\".branch WHERE branch_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

            pstmt.setInt(1, branchId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    branch = new Branch();
                    branch.setBranchId(rs.getInt("branch_id"));
                    branch.setBranchName(rs.getString("branch_name"));
                    branch.setBranchAddressId(rs.getInt("branch_address_id"));
                    branch.setBankId(rs.getInt("bank_id"));
                    branch.setIfscCode(rs.getString("ifsc_code"));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }
        return branch;
    }

    @Override
    public int deleteBranch(Integer branchId) {
        String deleteQuery = "DELETE FROM \"bankSchema\".branch WHERE branch_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, branchId);
            return pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }
        return 0;
    }
}
