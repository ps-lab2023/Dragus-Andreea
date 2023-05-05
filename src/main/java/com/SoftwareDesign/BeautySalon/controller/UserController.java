package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.dto.ClientDTO;
import com.SoftwareDesign.BeautySalon.dto.EmployeeDTO;
import com.SoftwareDesign.BeautySalon.dto.UserDTO;
import com.SoftwareDesign.BeautySalon.email.EmailService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.saleCode.SalesCodeGenerator;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.EmployeeService;
import com.SoftwareDesign.BeautySalon.service.UserService;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.EmployeeNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private  ModelMapper modelMapper;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public UserController(UserService userService, ClientService clientService, EmployeeService employeeService, EmailService emailService) {
        this.userService = userService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.emailService = emailService;
    }

    @GetMapping({"/getById"})
    public ResponseEntity<UserDTO> getUserById(@RequestParam long id) throws UserNotFoundException {
        User user = userService.getUserById(id);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping({"/getByName"})
    public ResponseEntity<UserDTO> getUserByName(@RequestParam String name) throws UserNotFoundException {
        User user = userService.getUserByName(name);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping({"/getByUserName"})
    public ResponseEntity<UserDTO> getUserByUserName(@RequestParam String userName) throws UserNotFoundException {
        User user = userService.getUserByUserName(userName);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping({"/getByUserNameAndPassword"})
    public ResponseEntity<UserDTO> getUserByUserNameAndPassword(@RequestParam String userName, @RequestParam String password) throws UserNotFoundException {
        User user = userService.getUserByUserNameAndPassword(userName, password);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping({"/getAll"})
    public ResponseEntity<List<UserDTO>> getAllUsers() throws UserNotFoundException {
        return ResponseEntity.ok().body(userService.getAllUsers().stream().map(user ->modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList()));

    }

    @PostMapping(path = "/add")
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) throws  DataBaseFailException, InvalidUserException {
        User user = modelMapper.map(userDTO, User.class);
        User addedUser = userService.addUser(user);
        UserDTO userResponse = modelMapper.map(addedUser,UserDTO.class);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/login")
    public ResponseEntity login(@RequestParam String userName, @RequestParam String password) throws UserNotFoundException, EmployeeNotFoundException, ClientNotFoundException {
        User user = userService.getUserByUserNameAndPassword(userName, password);
        UserDTO userResponse = modelMapper.map(user, UserDTO.class);

        if(user.getUserType().equals(UserType.EMPLOYEE)) {
            Employee employee = employeeService.getEmployeeById(user.getId());
            EmployeeDTO employeeResponse = modelMapper.map(employee, EmployeeDTO.class);
            return new ResponseEntity<>(employeeResponse, HttpStatus.OK);
        }

        if(user.getUserType().equals(UserType.CLIENT)) {
            Client client = clientService.getClientById(user.getId());
            ClientDTO clientResponse = modelMapper.map(client, ClientDTO.class);
            return new ResponseEntity<>(clientResponse, HttpStatus.OK);
        }

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) throws InvalidUserException, UserNotFoundException, DataBaseFailException {
        User userRequest = modelMapper.map(userDTO, User.class);
        User updatedUser = userService.updateUser(userRequest);
        UserDTO userResponse = modelMapper.map(updatedUser,UserDTO.class);

        return ResponseEntity.ok().body(userResponse);
    }

    @PutMapping("/logOut")
    public ResponseEntity<Map<String, Object>> logOut(@RequestBody String username) throws UserNotFoundException, DataBaseFailException, InvalidUserException {
        User userRequest = userService.getUserByUserName(username);
        userRequest.setLoggedIn(false);
        User updatedUser = userService.updateUser(userRequest);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "User " + username + " has logged out");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam Long id) throws UserNotFoundException, ClientNotFoundException, EmployeeNotFoundException {
        userService.deleteUserById(id);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Deleted successfully user with id = " + id);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/recoverPassword")
    public ResponseEntity<Map<String, Object>> recoverPassword(@RequestBody String userName) throws UserNotFoundException, MessagingException, DataBaseFailException, InvalidUserException {
        User user = this.userService.getUserByUserName(userName);
        SalesCodeGenerator salesCodeGenerator = new SalesCodeGenerator();
        String temporaryPassword = salesCodeGenerator.generateSalesCode();
        user.setPassword(passwordEncoder.encode(temporaryPassword));

        User updatedUser = this.userService.updateUser(user);
        emailService.sendPasswordRecovery(user.getUserName(),temporaryPassword);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Recovery password sent successfully to user" + userName);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
