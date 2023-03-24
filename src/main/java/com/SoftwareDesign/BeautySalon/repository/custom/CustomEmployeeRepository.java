package com.SoftwareDesign.BeautySalon.repository.custom;

import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;

import java.util.Optional;

public interface CustomEmployeeRepository {
    Optional<Employee> saveIfValid(Employee employee) throws InvalidEmployeeException, InvalidUserException;
}
