package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping({"/{id}"})
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) throws EmployeeNotFoundException {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @GetMapping({"/{name}"})
    public ResponseEntity<Employee> getEmployeeByName(@PathVariable String name) throws EmployeeNotFoundException {
        return new ResponseEntity<>(employeeService.getEmployeeByName(name),  HttpStatus.OK);
    }

    @GetMapping({"/{userName}"})
    public ResponseEntity<Employee> getEmployeeByUserName(@PathVariable String userName) throws EmployeeNotFoundException {
        return new ResponseEntity<>(employeeService.getEmployeeByUserName(userName), HttpStatus.OK);
    }

    @PostMapping(path = "employee",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) throws DataBaseFailException, InvalidUserException, InvalidEmployeeException {
        employeeService.addEmployee(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }


}
