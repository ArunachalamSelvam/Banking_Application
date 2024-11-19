package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.banking.db.DbManager;
import com.banking.entities.Country;
import com.banking.service.CountryService;

public class CountryServiceImpl implements CountryService {

    private static final DbManager DB_MANAGER = DbManager.getInstance();
    
    public static CountryServiceImpl instance = null;
    
    private CountryServiceImpl() {
		// TODO Auto-generated constructor stub
	}
    
    public static CountryServiceImpl getInstance() {
    	if(instance == null) {
    		instance = new CountryServiceImpl();
    	}
    	
    	return instance;
    }

    @Override
    public Country saveCountry(Country country) {
        String insertQuery = "INSERT INTO \"bankSchema\".country (country_name, active_status) VALUES (?, ?)";
        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, country.getCountryName());
            pstmt.setBoolean(2, country.getActiveStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        country.setCountryId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
        return country;
    }

    @Override
    public Country updateCountry(Integer countryId, Country country) {
        String updateQuery = "UPDATE \"bankSchema\".country SET country_name = ?, active_status = ? WHERE country_id = ?";
        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

            pstmt.setString(1, country.getCountryName());
            pstmt.setBoolean(2, country.getActiveStatus());
            pstmt.setInt(3, countryId);

            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
        return country;
    }

    @Override
    public List<Country> getAllCountry() {
        List<Country> countries = new ArrayList<>();
        String selectQuery = "SELECT * FROM \"bankSchema\".country";
        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Country country = new Country();
                country.setCountryId(rs.getInt("country_id"));
                country.setCountryName(rs.getString("country_name"));
                country.setActiveStatus(rs.getBoolean("active_status"));
                countries.add(country);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
        return countries;
    }

    @Override
    public Country getCountryById(Integer countryId) {
        Country country = null;
        String selectQuery = "SELECT * FROM \"bankSchema\".country WHERE country_id = ?";
        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

            pstmt.setInt(1, countryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    country = new Country();
                    country.setCountryId(rs.getInt("country_id"));
                    country.setCountryName(rs.getString("country_name"));
                    country.setActiveStatus(rs.getBoolean("active_status"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
        return country;
    }

    @Override
    public int deleteCountry(Integer countryId) {
        String deleteQuery = "DELETE FROM \"bankSchema\".country WHERE country_id = ?";
        int affectedRows = 0;
        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, countryId);
            affectedRows = pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
        return affectedRows;
    }
}
