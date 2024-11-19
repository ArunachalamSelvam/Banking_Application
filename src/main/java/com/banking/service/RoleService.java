package com.banking.service;


import com.banking.entities.Role;
import java.util.List;

public interface RoleService {
    Role getRoleById(Integer roleId);
    List<Role> getAllRoles();
    boolean addRole(Role role);
    boolean updateRole(Role role);
    boolean deleteRole(Integer roleId);
}
