package com.banking.serviceImpl;

import com.banking.db.DbManager;
import com.banking.entities.Role;
import com.banking.service.RoleService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl implements RoleService {

    private final DbManager DB_MANAGER = DbManager.getInstance();

    @Override
    public Role getRoleById(Integer roleId) {
        Role role = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DB_MANAGER.createConnection();
            String query = "SELECT * FROM \"bankSchema\".role WHERE role_id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                role = new Role();
                role.setRoleId(rs.getInt("role_id"));
                role.setRole(rs.getString("role"));
                role.setActiveStatus(rs.getBoolean("active_status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of creation
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DB_MANAGER.createConnection();
            String query = "SELECT * FROM \"bankSchema\".role";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("role_id"));
                role.setRole(rs.getString("role"));
                role.setActiveStatus(rs.getBoolean("active_status"));
                roles.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of creation
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roles;
    }

    @Override
    public boolean addRole(Role role) {
        boolean isAdded = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DB_MANAGER.createConnection();
            String query = "INSERT INTO \"bankSchema\".role(\"role\", active_status) VALUES (?, ?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, role.getRole());
            pstmt.setBoolean(2, role.isActiveStatus());
            int rowsAffected = pstmt.executeUpdate();
            isAdded = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of creation
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isAdded;
    }

    @Override
    public boolean updateRole(Role role) {
        boolean isUpdated = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DB_MANAGER.createConnection();
            String query = "UPDATE \"bankSchema\".role SET \"role\" = ?, active_status = ? WHERE role_id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, role.getRole());
            pstmt.setBoolean(2, role.isActiveStatus());
            pstmt.setInt(3, role.getRoleId());
            int rowsAffected = pstmt.executeUpdate();
            isUpdated = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of creation
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        boolean isDeleted = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DB_MANAGER.createConnection();
            String query = "DELETE FROM \"bankSchema\".role WHERE role_id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, roleId);
            int rowsAffected = pstmt.executeUpdate();
            isDeleted = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of creation
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isDeleted;
    }
}
