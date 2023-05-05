package com.SoftwareDesign.BeautySalon.controller;

import com.SoftwareDesign.BeautySalon.dto.AppointmentDTO;
import com.SoftwareDesign.BeautySalon.dto.ClientDTO;
import com.SoftwareDesign.BeautySalon.dto.EmployeeDTO;
import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidEmployeeException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.ClientService;
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
@RequestMapping("/client")
@CrossOrigin(origins = "http://localhost:4200")
public class ClientController {
    private ClientService clientService;
    @Autowired
    private ModelMapper modelMapper;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/getById"})
    public ResponseEntity<ClientDTO> getClientById(@RequestParam long id) throws ClientNotFoundException {
        Client client = clientService.getClientById(id);
        ClientDTO clientResponse = modelMapper.map(client, ClientDTO.class);

        return ResponseEntity.ok().body(clientResponse);
    }

    @GetMapping({"/getByName"})
    public ResponseEntity<ClientDTO> getClientByName(@RequestParam String name) throws ClientNotFoundException {
        Client client = clientService.getClientByName(name);
        ClientDTO clientResponse = modelMapper.map(client, ClientDTO.class);

        return ResponseEntity.ok().body(clientResponse);
    }

    @GetMapping({"/getByUserName"})
    public ResponseEntity<ClientDTO> getClientByUserName(@RequestParam String userName) throws ClientNotFoundException {
        Client client = clientService.getClientByUserName(userName);
        ClientDTO clientResponse = modelMapper.map(client, ClientDTO.class);

        return ResponseEntity.ok().body(clientResponse);
    }

    @GetMapping({"/getAll"})
    public ResponseEntity<List<ClientDTO>> getAllClients() throws ClientNotFoundException {
        return ResponseEntity.ok().body(clientService.getAllClients().stream().map(client ->modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList()));

    }

    @GetMapping({"/getByAppointmentsIsContaining"})
    public ResponseEntity<ClientDTO> getClientByAppointmentsIsContaining(@RequestBody AppointmentDTO appointmentDTO) throws ClientNotFoundException {
        Appointment appointmentRequest = modelMapper.map(appointmentDTO, Appointment.class);
        Client client = clientService.getClientByAppointmentsIsContaining(appointmentRequest);
        ClientDTO clientResponse = modelMapper.map(client, ClientDTO.class);

        return ResponseEntity.ok().body(clientResponse);
    }

    @GetMapping({"/getAllByLoyaltyPointsGreaterThan"})
    public ResponseEntity<List<ClientDTO>> getAllByLoyaltyPointsGreaterThan(@RequestParam int loyaltyPoints) throws ClientNotFoundException {
        return ResponseEntity.ok().body(clientService.getAllClientsByLoyaltyPointsGreaterThan(loyaltyPoints).stream().map(client ->modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList()));
    }

    @PostMapping("/add")
    public ResponseEntity<ClientDTO> addClient(@RequestBody ClientDTO clientDTO) throws DataBaseFailException, InvalidClientException, InvalidUserException {
        Client client = modelMapper.map(clientDTO, Client.class);
        Client addedClient = clientService.addClient(client);
        ClientDTO clientResponse = modelMapper.map(addedClient, ClientDTO.class);

        return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<ClientDTO> updateClient(@RequestBody ClientDTO clientDTO) throws InvalidUserException, UserNotFoundException, DataBaseFailException, ClientNotFoundException, InvalidClientException {
        Client clientRequest = modelMapper.map(clientDTO, Client.class);
        Client updatedClient = clientService.updateClient(clientRequest);
        ClientDTO clientResponse = modelMapper.map(updatedClient, ClientDTO.class);

        return ResponseEntity.ok().body(clientResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteClient(@RequestParam Long id) throws UserNotFoundException, ClientNotFoundException, EmployeeNotFoundException {
        clientService.deleteClientById(id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Deleted successfully client with id = " + id);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping("/logOut")
    public ResponseEntity<Map<String, Object>> logOut(@RequestBody String username) throws UserNotFoundException, DataBaseFailException, InvalidUserException, ClientNotFoundException, InvalidClientException {
        Client client = clientService.getClientByUserName(username);
        client.setLoggedIn(false);
        Client updatedClient = clientService.updateClient(client);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Client " + username + " has logged out");

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
