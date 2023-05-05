package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.ClientRepository;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.UserService;
import com.SoftwareDesign.BeautySalon.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private ClientRepository clientRepository;
    @Autowired
    private UserService userService;


    public ClientServiceImpl(ClientRepository clientRepository, UserService userService) {
        this.clientRepository = clientRepository;
        this.userService = userService;

    }
    @Override
    public Client addClient(Client client) throws InvalidClientException, DataBaseFailException, InvalidUserException {
        Optional<Client> clientWithSameUserName = this.clientRepository.findByUserName(client.getUserName());
        if(clientWithSameUserName.isEmpty()) {
        Optional<Client> clientOptional = this.clientRepository.saveIfValid(client);

        if(clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new DataBaseFailException("Failed to load client in DB");
        }
        } else {
            throw new DataBaseFailException("Username already exists in DB");
        }
    }

    @Override
    public Client getClientById(Long id) throws ClientNotFoundException {
        Optional<Client> clientOptional = this.clientRepository.findById(id);
        if(clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new ClientNotFoundException("Client not found by id = " + id);
        }
    }

    @Override
    public Client getClientByName(String name) throws ClientNotFoundException {
        Optional<Client> clientOptional = this.clientRepository.findByName(name);

        if(clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new ClientNotFoundException("Client not found by name =  "+ name);
        }
    }

    @Override
    public Client getClientByUserName(String username) throws ClientNotFoundException {
        Optional<Client> clientOptional = this.clientRepository.findByUserName(username);

        if(clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new ClientNotFoundException("Client not found by userName = " + username);
        }
    }

    @Override
    public Client getClientByAppointmentsIsContaining(Appointment appointment) throws ClientNotFoundException {
        Optional<Client> clientOptional = this.clientRepository.findByAppointmentsIsContaining(appointment);

        if(clientOptional.isPresent()) {
            return clientOptional.get();
        } else {
            throw new ClientNotFoundException("Client not found by appointment = " + appointment.toString());
        }

    }

    @Override
    public List<Client> getAllClientsByLoyaltyPointsGreaterThan(int points) throws ClientNotFoundException {
        List<Client> clientsOptional = this.clientRepository.findAllByLoyaltyPointsGreaterThan(points);
        if(! CollectionUtils.isEmpty(clientsOptional)) {
            return clientsOptional;
        } else {
            throw new ClientNotFoundException("Client not found by loyalty points greater than " + points);
        }
    }

    @Override
    public List<Client> getAllClients() throws ClientNotFoundException {
        List<Client> clientsOptional = this.clientRepository.findAll();
        if(!CollectionUtils.isEmpty(clientsOptional)) {
            return clientsOptional;
        } else {
            throw new ClientNotFoundException("Clients not found");
        }
    }

    @Override
    public Client updateClient(Client client) throws UserNotFoundException, DataBaseFailException, InvalidUserException, InvalidClientException, ClientNotFoundException {
        int loyaltyPoints = 0;
        User user = userService.updateUser(new User(client.getId(), client.getName(), UserType.CLIENT, client.getUserName(), client.getPassword(), client.isLoggedIn()));
        Optional<Client> client1 = clientRepository.findById(client.getId());
        if(client1.isPresent()) {
            if(client.getLoyaltyPoints() >= 0 || client.getLoyaltyPoints() <= 1000) {
                loyaltyPoints = client.getLoyaltyPoints();
            } else {
                loyaltyPoints = client1.get().getLoyaltyPoints();
            }
            Client updatedClient = new Client(client.getId(), user.getName(), user.getUserName(), user.getPassword(), loyaltyPoints);
            updatedClient.setSalesCode(client.getSalesCode());
            /*
            if(client.getAppointments() !=  null) {
                List<Appointment> appointments = appointmentService.getAllAppointmentsByClient(client1.get());
                updatedClient.setAppointments(appointments);
                for(Appointment appointment: client.getAppointments()) {
                    updatedClient.addAppointment(appointment);
                }

            }
            */
            updatedClient.setLoggedIn(client.isLoggedIn());
            Optional<Client> clientOptional = this.clientRepository.saveIfValid(updatedClient);

            if(clientOptional.isPresent()) {
                return clientOptional.get();
            } else {
                throw new DataBaseFailException("Failed to load updated client in DB");
            }
        } else {
            throw new ClientNotFoundException("Could not update Client because it was not found in DB");
        }
    }

    @Override
    public void deleteClientById(Long id) throws ClientNotFoundException, UserNotFoundException, AppointmentNotFoundException, EmployeeNotFoundException {
        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            this.clientRepository.deleteById(id);
            this.userService.deleteUserById(id);
        } else {
            throw new ClientNotFoundException("Could not delete client because it was not found in DB");
        }

    }
}
