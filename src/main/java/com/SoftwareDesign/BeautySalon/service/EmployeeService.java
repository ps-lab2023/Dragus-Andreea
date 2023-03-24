package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.exception.*;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(Employee employee) throws InvalidEmployeeException, DataBaseFailException, InvalidUserException;

    Employee getEmployeeById(Long id) throws EmployeeNotFoundException;

    Employee getEmployeeByName(String name) throws EmployeeNotFoundException;

    Employee getEmployeeByUserName(String username) throws  EmployeeNotFoundException;

    Employee getEmployeeByAppointmentsIsContaining(Appointment appointment) throws EmployeeNotFoundException;


    List<Employee> getAllEmployeesByEmployeeType(EmployeeType employeeType) throws EmployeeNotFoundException;

    Employee updateEmployee(Employee employee) throws UserNotFoundException, DataBaseFailException, InvalidUserException, EmployeeNotFoundException, InvalidEmployeeException;
    void deleteEmployeeById(Long id) throws EmployeeNotFoundException, AppointmentNotFoundException, UserNotFoundException, ClientNotFoundException;
}
