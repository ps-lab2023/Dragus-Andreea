package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClientService {
    Client addClient(Client client) throws InvalidClientException, DataBaseFailException, InvalidUserException;

    Client getClientById(Long id) throws ClientNotFoundException;

    Client getClientByName(String name) throws ClientNotFoundException;

    Client getClientByUserName(String username) throws ClientNotFoundException;

    Client getClientByAppointmentsIsContaining(Appointment appointment) throws ClientNotFoundException;

    List<Client> getAllClientsByLoyaltyPointsGreaterThan(int points) throws ClientNotFoundException;

    List<Client> getAllClients() throws ClientNotFoundException;
    Client updateClient(Client client) throws UserNotFoundException, DataBaseFailException, InvalidUserException, InvalidClientException, ClientNotFoundException;
    void deleteClientById(Long id) throws ClientNotFoundException, UserNotFoundException, AppointmentNotFoundException, EmployeeNotFoundException;
}
