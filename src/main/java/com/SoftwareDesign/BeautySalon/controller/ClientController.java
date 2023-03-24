package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Client> getClientById(@PathVariable long id) throws ClientNotFoundException {
        return new ResponseEntity<>(clientService.getClientById(id), HttpStatus.OK);
    }

    @GetMapping({"/{name}"})
    public ResponseEntity<Client> getClientByName(@PathVariable String name) throws ClientNotFoundException {
        return new ResponseEntity<>(clientService.getClientByName(name),  HttpStatus.OK);
    }

    @GetMapping({"/{userName}"})
    public ResponseEntity<Client> getClientByUserName(@PathVariable String userName) throws ClientNotFoundException {
        return new ResponseEntity<>(clientService.getClientByUserName(userName), HttpStatus.OK);
    }

    @PostMapping(path = "client",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> addClient(@RequestBody Client client) throws DataBaseFailException, InvalidClientException, InvalidUserException {
        clientService.addClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }
}
