package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.ClientRepository;
import com.SoftwareDesign.BeautySalon.repository.EmployeeRepository;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomClientRepository;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.UserService;
import com.SoftwareDesign.BeautySalon.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) throws InvalidUserException, DataBaseFailException {
        Optional<User> userWithSameUserName = userRepository.findByUserName(user.getUserName());

        if(userWithSameUserName.isEmpty()) {
            Optional<User> userOptional = this.userRepository.saveIfValid(user);
            if (userOptional.isPresent()) {
                return userOptional.get();
            } else {
                throw new DataBaseFailException("Failed to load user in DB");
            }
        } else {
            throw new DataBaseFailException("Username already exists in DB");
        }
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> userOptional = this.userRepository.findById(id);

        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found by id = "+ id);
        }

    }

    @Override
    public User getUserByName(String name) throws UserNotFoundException {
        Optional<User> userOptional = this.userRepository.findByName(name);

        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found by name =  "+ name);
        }
    }

    @Override
    public User getUserByUserName(String username) throws UserNotFoundException {
        Optional<User> userOptional = this.userRepository.findByUserName(username);

        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found by userName = " + username);
        }
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException, InvalidUserException, DataBaseFailException {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if(userOptional.isPresent()) {
            User updatedUser = userOptional.get();

            if(!user.getName().equals("") && user.getName() != null) {
                updatedUser.setName(user.getName());
            }

            if(!user.getUserName().equals("") && user.getUserName() != null) {
                updatedUser.setUserName(user.getUserName());
            }

            if(!user.getPassword().equals("") && user.getPassword() != null) {
                updatedUser.setPassword(user.getPassword());
            }

            Optional<User> userOptionalUpdated = this.userRepository.saveIfValid(updatedUser);
            if (userOptionalUpdated.isPresent()) {
                return userOptionalUpdated.get();
            } else {
                throw new DataBaseFailException("Failed to load updated user in DB");
            }

        } else {
            throw new UserNotFoundException("Could not update user because it was not found in DB");
        }
    }

    @Override
    public List<User> getAllByUserType(UserType userType) throws UserNotFoundException {
        List<User> usersOptional = this.userRepository.findAllByUserType(userType);
        if(!CollectionUtils.isEmpty(usersOptional)) {
            return usersOptional;
        } else {
            throw new UserNotFoundException("Users not found by userType = " + userType.toString());
        }

    }

    @Override
    public void deleteUserById(Long id) throws UserNotFoundException, ClientNotFoundException, EmployeeNotFoundException, AppointmentNotFoundException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("Could not delete user because it was not found in User DB");
        }


    }
}
