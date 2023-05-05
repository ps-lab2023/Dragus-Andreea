package com.SoftwareDesign.BeautySalon.security.auth;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.security.config.JwtService;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.UserService;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.EmployeeNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService service;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws InvalidUserException, DataBaseFailException, InvalidUserException, InvalidClientException, InvalidEmployeeException {
        UserType userType = null;
        var jwtToken = "";
        if(request.getUserType().equals("ADMIN")) {
            userType = UserType.ADMIN;
            var user = User.builder()
                    .name(request.getName())
                    .userName(request.getUserName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .userType(userType)
                    .build();

            User savedUser = service.addUser(user);

            jwtToken = jwtService.generateToken(savedUser);
        }

        if(request.getUserType().equals("CLIENT")) {
            userType = UserType.CLIENT;
            var client = Client.builder()
                    .name(request.getName())
                    .userName(request.getUserName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .userType(userType)
                    .loyaltyPoints(0)
                    .build();

            Client savedClient = clientService.addClient(client);

            jwtToken = jwtService.generateToken(savedClient);
        }

        if(request.getUserType().equals("EMPLOYEE")) {
            userType = UserType.EMPLOYEE;
            EmployeeType employeeType = null;

            if(request.getEmployeeType().equals("NAIL_TECH")) {
                employeeType = EmployeeType.NAIL_TECH;
            }

            if(request.getEmployeeType().equals("MAKEUP_ARTIST")) {
                employeeType = EmployeeType.MAKEUP_ARTIST;
            }

            if(request.getEmployeeType().equals("HAIR_DRESSER")) {
                employeeType = EmployeeType.HAIR_DRESSER;
            }

            var employee = Employee.builder()
                    .name(request.getName())
                    .userName(request.getUserName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .userType(userType)
                    .employeeType(employeeType)
                    .build();

            Employee savedEmployee = employeeService.addEmployee(employee);

            jwtToken = jwtService.generateToken(savedEmployee);
        }




        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws UserNotFoundException, DataBaseFailException, InvalidUserException, EmployeeNotFoundException, InvalidEmployeeException, ClientNotFoundException, InvalidClientException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        User foundUser  = service.getUserByUserName(request.getUserName());
        foundUser.setLoggedIn(true);
        service.updateUser(foundUser);

        if(foundUser.getUserType().equals(UserType.EMPLOYEE)) {
            Employee foundEmployee = employeeService.getEmployeeByUserName(request.getUserName());
            foundEmployee.setLoggedIn(true);
            employeeService.updateEmployee(foundEmployee);
        }

        if(foundUser.getUserType().equals(UserType.CLIENT)) {
            Client foundClient = clientService.getClientByUserName(request.getUserName());
            foundClient.setLoggedIn(true);
            clientService.updateClient(foundClient);
        }

        var jwtToken = jwtService.generateToken(foundUser);

        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();


    }
}
