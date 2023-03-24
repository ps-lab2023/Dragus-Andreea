package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.exception.*;

import java.util.List;


public interface UserService {
    User addUser(User User) throws InvalidUserException, DataBaseFailException;

    User getUserById(Long id) throws UserNotFoundException;

    User getUserByName(String name) throws UserNotFoundException;

    User getUserByUserName(String username) throws UserNotFoundException;

    User updateUser(User user) throws UserNotFoundException, InvalidUserException, DataBaseFailException;

    List<User> getAllByUserType(UserType userType) throws UserNotFoundException;

    void deleteUserById(Long id) throws UserNotFoundException, ClientNotFoundException, EmployeeNotFoundException, AppointmentNotFoundException;
}