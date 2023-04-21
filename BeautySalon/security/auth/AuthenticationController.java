package com.SoftwareDesign.BeautySalon.security.auth;

import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws InvalidUserException, DataBaseFailException, InvalidClientException, InvalidEmployeeException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws UserNotFoundException {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
