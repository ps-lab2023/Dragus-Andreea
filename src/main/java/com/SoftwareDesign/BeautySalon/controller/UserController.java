package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.UserService;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<User> getUserById(@PathVariable long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping({"/{name}"})
    public ResponseEntity<User> getUserByName(@PathVariable String name) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserByName(name),  HttpStatus.OK);
    }

    @GetMapping({"/{userName}"})
    public ResponseEntity<User> getUserByUserName(@PathVariable String userName) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserByUserName(userName), HttpStatus.OK);
    }

    @PostMapping(path = "user",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) throws  DataBaseFailException, InvalidUserException {
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
