package com.banking.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.banking.db.DbManager;
import com.banking.entities.State;
import com.banking.service.StateService;

public class StateServiceImpl implements StateService {

    private static final DbManager DB_MANAGER = DbManager.getInstance();

    
public static StateServiceImpl instance = null;
    
    private StateServiceImpl() {
		// TODO Auto-generated constructor stub
	}
    
    public static StateServiceImpl getInstance() {
    	if(instance == null) {
    		instance = new StateServiceImpl();
    	}
    	
    	return instance;
    }
    @Override
    public State saveState(State state) {
        String insertQuery = "INSERT INTO \"bankSchema\".state (country_id, state_name, active_status) VALUES (?, ?, ?)";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, state.getCountryId());
            pstmt.setString(2, state.getStateName());
            pstmt.setBoolean(3, state.getActiveStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        state.setStateId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }

        return state;
    }

    @Override
    public State updateState(Integer stateId, State state) {
        String updateQuery = "UPDATE \"bankSchema\".state SET country_id = ?, state_name = ?, active_status = ? WHERE state_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {

            pstmt.setInt(1, state.getCountryId());
            pstmt.setString(2, state.getStateName());
            pstmt.setBoolean(3, state.getActiveStatus());
            pstmt.setInt(4, stateId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                state.setStateId(stateId);
                return state;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }

        return null; // Return null if update fails
    }

    @Override
    public List<State> getAllStates() {
        String selectQuery = "SELECT * FROM \"bankSchema\".state";
        List<State> stateList = new ArrayList<>();

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                State state = new State();
                state.setStateId(rs.getInt("state_id"));
                state.setCountryId(rs.getInt("country_id"));
                state.setStateName(rs.getString("state_name"));
                state.setActiveStatus(rs.getBoolean("active_status"));

                stateList.add(state);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }

        return stateList;
    }

    @Override
    public State getStateById(Integer stateId) {
        String selectQuery = "SELECT * FROM \"bankSchema\".state WHERE state_id = ?";
        State state = null;

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

            pstmt.setInt(1, stateId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    state = new State();
                    state.setStateId(rs.getInt("state_id"));
                    state.setCountryId(rs.getInt("country_id"));
                    state.setStateName(rs.getString("state_name"));
                    state.setActiveStatus(rs.getBoolean("active_status"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }

        return state;
    }
    
    @Override
    public  List<State> getStatesByCountryId(Integer countryId) {
        String selectQuery = "SELECT * FROM \"bankSchema\".state WHERE country_id = ?";
//        State state = null;
        
        List<State>stateList = new ArrayList<>();

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(selectQuery)) {

            pstmt.setInt(1, countryId);

            try (ResultSet rs = pstmt.executeQuery()) {
              
                	 while (rs.next()) {
                         State state = new State();
                         state.setStateId(rs.getInt("state_id"));
                         state.setCountryId(rs.getInt("country_id"));
                         state.setStateName(rs.getString("state_name"));
                         state.setActiveStatus(rs.getBoolean("active_status"));

                         stateList.add(state);
                     }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }

        return stateList;
    }


    @Override
    public int deleteState(Integer stateId) {
        String deleteQuery = "DELETE FROM \"bankSchema\".state WHERE state_id = ?";

        try (Connection con = DB_MANAGER.createConnection();
             PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, stateId);

            return pstmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exception as needed
        }

        return 0; // Return 0 if deletion fails
    }
}
