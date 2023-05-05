package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.EmployeeRepository;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.UserService;
import com.SoftwareDesign.BeautySalon.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserService userService, AppointmentService appointmentService )  {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @Override
    public Employee addEmployee(Employee employee) throws InvalidEmployeeException, DataBaseFailException, InvalidUserException {
        Optional<Employee> employeeWithSameUserName = employeeRepository.findByUserName(employee.getUserName());

        if(employeeWithSameUserName.isEmpty()) {
            Optional<Employee> employeeOptional = this.employeeRepository.saveIfValid(employee);

            if (employeeOptional.isPresent()) {
                return employeeOptional.get();
            } else {
                throw new DataBaseFailException("Failed to load employee in DB");
            }
        } else {
            throw new DataBaseFailException("Username already exists in DB");
        }
    }

    @Override
    public Employee getEmployeeById(Long id) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeRepository.findById(id);
        if(employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new EmployeeNotFoundException("Employee not found by id = " + id);
        }
    }

    @Override
    public Employee getEmployeeByName(String name) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeRepository.findByName(name);

        if(employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new EmployeeNotFoundException("Employee not found by name =  "+ name);
        }
    }

    @Override
    public Employee getEmployeeByUserName(String username) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeRepository.findByUserName(username);

        if(employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new EmployeeNotFoundException("Employee not found by userName = " + username);
        }
    }

    @Override
    public Employee getEmployeeByAppointmentsIsContaining(Appointment appointment) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = this.employeeRepository.findByAppointmentsIsContaining(appointment);

        if(employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new EmployeeNotFoundException("Employee not found by appointment = " + appointment.toString());
        }
    }


    @Override
    public List<Employee> getAllEmployeesByEmployeeType(EmployeeType employeeType) throws EmployeeNotFoundException {
        List<Employee> employeesOptional = this.employeeRepository.findAllByEmployeeType(employeeType);

        if(!CollectionUtils.isEmpty(employeesOptional)) {
            return employeesOptional;
        } else {
            throw new EmployeeNotFoundException("Employee not found by employee type = " + employeeType.toString());
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws EmployeeNotFoundException {
        List<Employee> employeesOptional = this.employeeRepository.findAll();

        if(!CollectionUtils.isEmpty(employeesOptional)) {
            return employeesOptional;
        } else {
            throw new EmployeeNotFoundException("Employees not found ");
        }
    }

    @Override
    public Employee updateEmployee(Employee employee) throws UserNotFoundException, DataBaseFailException, InvalidUserException, EmployeeNotFoundException, InvalidEmployeeException {
        User user = userService.updateUser(new User(employee.getId(), employee.getName(), UserType.EMPLOYEE, employee.getUserName(), employee.getPassword(), employee.isLoggedIn()));
        Optional<Employee> employee1 = employeeRepository.findById(employee.getId());
        if(employee1.isPresent()) {

            Employee updatedEmployee = new Employee(employee.getId(), user.getName(), UserType.EMPLOYEE, user.getUserName(), user.getPassword(), employee1.get().getEmployeeType());

            if(employee.getAppointments() != null && employee.getAppointments().size() > 0) {
                List<Appointment> appointments = appointmentService.getAllAppointmentsByEmployeeId(employee1.get().getId());
                updatedEmployee.setAppointments(appointments);
                for(Appointment appointment: employee.getAppointments()) {
                    updatedEmployee.addAppointment(appointment);
                }
            }

            if(!employee.getEmployeeType().equals(employee1.get().getEmployeeType())) {
             updatedEmployee.setEmployeeType(employee.getEmployeeType());
            }

            updatedEmployee.setLoggedIn(employee.isLoggedIn());
            Optional<Employee> employeeOptional = this.employeeRepository.saveIfValid(updatedEmployee);

            if (employeeOptional.isPresent()) {
                return employeeOptional.get();
            } else {
                throw new DataBaseFailException("Failed to load updated employee in DB");
            }
        } else {
            throw new EmployeeNotFoundException("Could not update Employee because it was not found in DB");
        }

    }

    @Override
    public void deleteEmployeeById(Long id) throws EmployeeNotFoundException, UserNotFoundException, ClientNotFoundException, AppointmentNotFoundException {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            this.employeeRepository.deleteById(id);
            this.userService.deleteUserById(id);
        } else {
            throw new EmployeeNotFoundException("Could not delete employee because it was not found in DB");
        }
    }
}
