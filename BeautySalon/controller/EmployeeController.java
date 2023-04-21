package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.dto.AppointmentDTO;
import com.SoftwareDesign.BeautySalon.dto.EmployeeDTO;
import com.SoftwareDesign.BeautySalon.dto.UserDTO;
import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.EmployeeNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping({"/getById"})
    public ResponseEntity<EmployeeDTO> getEmployeeById(@RequestParam long id) throws EmployeeNotFoundException {
        Employee employee = employeeService.getEmployeeById(id);
        EmployeeDTO employeeResponse = modelMapper.map(employee, EmployeeDTO.class);

        return ResponseEntity.ok().body(employeeResponse);
    }

    @GetMapping({"/getByName"})
    public ResponseEntity<EmployeeDTO> getEmployeeByName(@RequestParam String name) throws EmployeeNotFoundException {
        Employee employee = employeeService.getEmployeeByName(name);
        EmployeeDTO employeeResponse = modelMapper.map(employee, EmployeeDTO.class);

        return ResponseEntity.ok().body(employeeResponse);
    }

    @GetMapping({"/getByUserName"})
    public ResponseEntity<EmployeeDTO> getEmployeeByUserName(@RequestParam String userName) throws EmployeeNotFoundException {
        Employee employee = employeeService.getEmployeeByUserName(userName);
        EmployeeDTO employeeResponse = modelMapper.map(employee, EmployeeDTO.class);

        return ResponseEntity.ok().body(employeeResponse);
    }

    @GetMapping({"/getAll"})
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() throws EmployeeNotFoundException {
        return ResponseEntity.ok().body(employeeService.getAllEmployees().stream().map(employee ->modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList()));

    }

    @GetMapping({"/getByAppointmentsIsContaining"})
    public ResponseEntity<EmployeeDTO> getEmployeeByAppointmentsIsContaining(@RequestBody AppointmentDTO appointmentDTO) throws EmployeeNotFoundException {
        Appointment appointmentRequest = modelMapper.map(appointmentDTO, Appointment.class);
        Employee employee = employeeService.getEmployeeByAppointmentsIsContaining(appointmentRequest);
        EmployeeDTO employeeResponse = modelMapper.map(employee, EmployeeDTO.class);

        return ResponseEntity.ok().body(employeeResponse);
    }

    @GetMapping({"/getByEmployeeType"})
    public ResponseEntity<List<EmployeeDTO>> getEmployeeByEmployeeType(@RequestParam EmployeeType employeeType) throws EmployeeNotFoundException {
        return ResponseEntity.ok().body(employeeService.getAllEmployeesByEmployeeType(employeeType).stream().map(employee ->modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList()));
    }


    @PostMapping(path = "/add")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) throws DataBaseFailException, InvalidUserException, InvalidEmployeeException {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        Employee addedEmployee = employeeService.addEmployee(employee);
        EmployeeDTO employeeResponse = modelMapper.map(addedEmployee,EmployeeDTO.class);

        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO) throws InvalidUserException, UserNotFoundException, DataBaseFailException, InvalidEmployeeException, EmployeeNotFoundException {
        Employee employeeRequest = modelMapper.map(employeeDTO, Employee.class);
        Employee updatedEmployee = employeeService.updateEmployee(employeeRequest);
        EmployeeDTO employeeResponse = modelMapper.map(updatedEmployee, EmployeeDTO.class);

        return ResponseEntity.ok().body(employeeResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@RequestParam Long id) throws UserNotFoundException, ClientNotFoundException, EmployeeNotFoundException {
        employeeService.deleteEmployeeById(id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Deleted successfully employee with id = " + id);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }


}
