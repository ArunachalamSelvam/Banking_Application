package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.Address;
import com.banking.service.AddressService;

public class AddressServiceImpl implements AddressService {

    private final DbManager DB_MANAGER = DbManager.getInstance();

    
public static AddressServiceImpl instance = null;
    
    private AddressServiceImpl() {
		// TODO Auto-generated constructor stub
	}
    
    public static AddressServiceImpl getInstance() {
    	if(instance == null) {
    		instance = new AddressServiceImpl();
    	}
    	
    	return instance;
    }
    @Override
    public Address saveAddress(Address address) throws SQLException {
        String insertQuery = "INSERT INTO \"bankSchema\".address (address, state_id, country_id, active_status) VALUES (?, ?, ?, ?)";
        Connection connection = null;
        try  {
        	connection = DB_MANAGER.createConnection();
            PreparedStatement pstmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, address.getAddress());
            pstmt.setInt(2, address.getStateId());
            pstmt.setInt(3, address.getCountryId());
            pstmt.setBoolean(4, address.getActiveStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        address.setAddressId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        connection.close();
	        // Handle exception as needed
	    }

        return address;
    }

    @Override
    public Address updateAddress(Integer addressId, Address address) {
        String updateQuery = "UPDATE \"bankSchema\".address SET address = ?, state_id = ?, country_id = ?, active_status = ? WHERE address_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

            pstmt.setString(1, address.getAddress());
            pstmt.setInt(2, address.getStateId());
            pstmt.setInt(3, address.getCountryId());
            pstmt.setBoolean(4, address.getActiveStatus());
            pstmt.setInt(5, addressId);

            pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

        return address;
    }

    @Override
    public List<Address> getAllAddresses() {
        List<Address> addresses = new ArrayList<>();
        String selectQuery = "SELECT * FROM \"bankSchema\".address";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Address address = new Address();
                address.setAddressId(rs.getInt("address_id"));
                address.setAddress(rs.getString("address"));
                address.setStateId(rs.getInt("state_id"));
                address.setCountryId(rs.getInt("country_id"));
                address.setActiveStatus(rs.getBoolean("active_status"));
                addresses.add(address);
            }

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }
        return addresses;
    }

    @Override
    public Address getAddressById(Integer addressId) {
        Address address = null;
        String selectQuery = "SELECT * FROM \"bankSchema\".address WHERE address_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

            pstmt.setInt(1, addressId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    address = new Address();
                    address.setAddressId(rs.getInt("address_id"));
                    address.setAddress(rs.getString("address"));
                    address.setStateId(rs.getInt("state_id"));
                    address.setCountryId(rs.getInt("country_id"));
                    address.setActiveStatus(rs.getBoolean("active_status"));
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

        return address;
    }

    @Override
    public int deleteAddress(Integer addressId) {
        String deleteQuery = "DELETE FROM \"bankSchema\".address WHERE address_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, addressId);
            return pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	        // Handle exception as needed
	    }

        return 0;
    }
}
