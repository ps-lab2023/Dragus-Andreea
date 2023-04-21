package com.SoftwareDesign.BeautySalon.repository.custom.impl;

import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.model.validation.ClientValidator;
import com.SoftwareDesign.BeautySalon.model.validation.EmployeeValidator;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomEmployeeRepository;
import com.SoftwareDesign.BeautySalon.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;

import java.util.Optional;
@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Optional<Employee> saveIfValid(Employee employee) throws InvalidEmployeeException, InvalidUserException {
        DataBinder dataBinder = new DataBinder(employee);
        dataBinder.addValidators(new EmployeeValidator());
        dataBinder.validate();

        if(!dataBinder.getBindingResult().hasErrors()) {
            if(employee.getId() == null) {
                User user = new User(employee.getName(), UserType.EMPLOYEE, employee.getUserName(), employee.getPassword());
                userRepository.saveIfValid(user);
                Employee employee2 = new Employee(user.getId(), employee.getName(), employee.getUserType(), employee.getUserName(), employee.getPassword(), employee.getEmployeeType());
                entityManager.persist(employee2);
                return Optional.of(employee2);
            } else {
                entityManager.merge(employee);
                return Optional.of(employee);
            }
        } else {
            System.out.println(dataBinder.getBindingResult().getAllErrors());
            throw new InvalidEmployeeException();
        }
    }
}
