package com.banking.service;

import java.sql.SQLException;
import java.util.List;

import com.banking.entities.User;

public interface UserService {

    User saveUser(User user) throws ClassNotFoundException, SQLException;

    User updateUser(Integer userId, User user);

    List<User> getAllUsers();

    User getUserById(Integer userId);

    int deleteUser(Integer userId);
}
