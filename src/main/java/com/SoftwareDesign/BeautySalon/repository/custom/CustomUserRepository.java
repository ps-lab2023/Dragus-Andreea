package com.SoftwareDesign.BeautySalon.repository.custom;

import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;

import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> saveIfValid(User user) throws InvalidUserException;

}
