package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.TestDataBuilder;
import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.ClientRepository;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.EmployeeNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import com.SoftwareDesign.BeautySalon.service.impl.AppointmentServiceImpl;
import com.SoftwareDesign.BeautySalon.service.impl.ClientServiceImpl;
import com.SoftwareDesign.BeautySalon.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private AppointmentServiceImpl appointmentService;
    @InjectMocks
    private ClientServiceImpl clientService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenClientObject_whenAddValidClient_thenReturnClientObject() {
        Client validClient = TestDataBuilder.buildValidClient1();
        try {
            given(clientRepository.findByUserName(validClient.getUserName()))
                    .willReturn(Optional.empty());

            given(clientRepository.saveIfValid(validClient))
                    .willReturn(Optional.of(validClient));

            Assertions.assertDoesNotThrow(() -> clientService.addClient(validClient));

            then(clientRepository)
                    .should()
                    .saveIfValid(validClient);

        } catch(InvalidUserException | InvalidClientException exc) {
            fail("Should not throw exception");
        }

    }

    @Test
    public void givenInvalidClientObject_whenAddClient_thenThrowException() throws InvalidUserException, InvalidClientException {
        Client invalidClient = TestDataBuilder.buildInvalidClient();

        given(clientRepository.findByUserName(invalidClient.getUserName()))
                .willReturn(Optional.empty());

        willThrow(new InvalidClientException())
                .given(clientRepository)
                .saveIfValid(invalidClient);

        Assertions.assertThrows(InvalidClientException.class,  () -> clientService.addClient(invalidClient));
    }

    @Test
    public void givenExistingUsername_whenAddClient_thenThrowException() throws InvalidUserException, InvalidClientException {
        Client client = TestDataBuilder.buildValidClient1();

        given(clientRepository.findByUserName(client.getUserName()))
                .willReturn(Optional.of(client));

        Assertions.assertThrows(DataBaseFailException.class, () -> clientService.addClient(client));

        then(clientRepository)
                .should(never())
                .saveIfValid(client);

    }

    @Test
    public void givenClientId_whenGetClientById_thenReturnClientObject() {
        Client client = TestDataBuilder.buildValidClient1();
        client.setId(1L);

        given(clientRepository.findById(1L)).willReturn(Optional.of(client));

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundClient.set(clientService.getClientById(client.getId())));

        Assertions.assertEquals(foundClient.get(), client);

        then(clientRepository)
                .should()
                .findById(client.getId());

    }

    @Test
    public void givenNotExistingClientId_whenGetClientById_thenThrowsException() {
        given(clientRepository.findById(100L)).willReturn(Optional.empty());

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertThrows(ClientNotFoundException.class, () -> foundClient.set(clientService.getClientById(100L)));

        Assertions.assertNull(foundClient.get());

        then(clientRepository)
                .should()
                .findById(100L);

    }

    @Test
    public void givenName_whenGetClientByName_thenReturnClientObject() {
        Client client = TestDataBuilder.buildValidClient1();
        client.setId(1L);

        given(clientRepository.findByName(client.getName()))
                .willReturn(Optional.of(client));

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundClient.set(clientService.getClientByName(client.getName())));

        Assertions.assertEquals(foundClient.get(), client);

        then(clientRepository)
                .should()
                .findByName(client.getName());
    }

    @Test
    public void givenNotExistingName_whenGeClientByName_thenThrowException() {
        String notExistingName = "John Doe";

        given(clientRepository.findByName(notExistingName))
                .willReturn(Optional.empty());

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertThrows(ClientNotFoundException.class, () -> foundClient.set(clientService.getClientByName(notExistingName)));

        Assertions.assertNull(foundClient.get());

        then(clientRepository)
                .should()
                .findByName(notExistingName);

    }

    @Test
    public void givenUserName_whenGetClientByUserName_thenReturnClientObject() {
        Client client = TestDataBuilder.buildValidClient1();
        client.setId(1L);

        given(clientRepository.findByUserName(client.getUserName()))
                .willReturn(Optional.of(client));

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundClient.set(clientService.getClientByUserName(client.getUserName())));

        Assertions.assertEquals(foundClient.get(), client);

        then(clientRepository)
                .should()
                .findByUserName(client.getUserName());

    }

    @Test
    public void givenNotExistingUserName_whenGetClientByUserName_thenThrowException() {
        String notExistingUsername = "johnDoe@domain.com";

        given(clientRepository.findByUserName(notExistingUsername))
                .willReturn(Optional.empty());

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertThrows(ClientNotFoundException.class, () -> foundClient.set(clientService.getClientByUserName(notExistingUsername)));

        Assertions.assertNull(foundClient.get());

        then(clientRepository)
                .should()
                .findByUserName(notExistingUsername);
    }

    @Test
    public void givenAppointment_whenGetClientByAppointmentsIsContaining_thenReturnClient() {
        Client client = TestDataBuilder.buildValidClient2();
        client.setId(1L);

        Appointment appointment1 = TestDataBuilder.buildValidAppointment1();
        Appointment appointment2 = TestDataBuilder.buildValidAppointment3();

        client.addAppointment(appointment1);
        client.addAppointment(appointment2);

        given(clientRepository.findByAppointmentsIsContaining(appointment1))
                .willReturn(Optional.of(client));

        AtomicReference<Client> foundClient = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundClient.set(clientService.getClientByAppointmentsIsContaining(appointment1)));

        Assertions.assertEquals(client, foundClient.get());

        then(clientRepository)
                .should()
                .findByAppointmentsIsContaining(appointment1);
    }

    @Test
    public void givenLoyaltyPoints_whenGetClientByLoyaltyPointsGreaterThan_thenReturnClient() {
        int loyaltyPoints = 100;

        Client client1 = TestDataBuilder.buildValidClient1();
        client1.setId(1L);
        client1.setLoyaltyPoints(120);

        Client client2 = TestDataBuilder.buildValidClient2();
        client1.setId(2L);
        client1.setLoyaltyPoints(180);

        List<Client> expected = new ArrayList<>();
        expected.add(client1);
        expected.add(client2);

        given(clientRepository.findAllByLoyaltyPointsGreaterThan(loyaltyPoints))
                .willReturn(List.of(client1, client2));

        AtomicReference<List<Client>> foundClients = new AtomicReference<>();
        Assertions.assertDoesNotThrow(() -> foundClients.set(clientService.getAllClientsByLoyaltyPointsGreaterThan(loyaltyPoints)));

        Assertions.assertEquals(expected, foundClients.get());

        then(clientRepository)
                .should()
                .findAllByLoyaltyPointsGreaterThan(loyaltyPoints);
    }

    @Test
    public void givenClientObject_whenUpdateClient_thenReturnUpdatedClient() {
        Client savedClient = TestDataBuilder.buildValidClient1();
        savedClient.setId(1L);

        Client clientToBeUpdated = TestDataBuilder.buildValidClientToUpdate();
        clientToBeUpdated.setId(1L);

        User userToBeUpdated = new User(clientToBeUpdated.getId(), clientToBeUpdated.getName(), UserType.CLIENT, clientToBeUpdated.getUserName(), clientToBeUpdated.getPassword());

        try {
            given(userService.updateUser(userToBeUpdated))
                    .willReturn(userToBeUpdated);

            given(appointmentService.getAllAppointmentsByClientId(savedClient.getId()))
                    .willReturn(savedClient.getAppointments());

            given(clientRepository.findById(savedClient.getId()))
                    .willReturn(Optional.of(savedClient));

            given(clientRepository.saveIfValid(clientToBeUpdated))
                    .willReturn(Optional.of(clientToBeUpdated));

            AtomicReference<Client> updatedClient = new AtomicReference<>();
            Assertions.assertDoesNotThrow(() -> updatedClient.set(clientService.updateClient(clientToBeUpdated)));

            Assertions.assertEquals(updatedClient.get(), clientToBeUpdated);
            Assertions.assertEquals(updatedClient.get().getId(), savedClient.getId());

            then(clientRepository)
                    .should()
                    .saveIfValid(clientToBeUpdated);

        } catch(InvalidUserException | UserNotFoundException | DataBaseFailException | InvalidClientException exception) {
            fail("Should not throw exception at updating");
        }

    }

    @Test
    public void givenNotExistingClientObject_whenUpdateClient_thenThrowException() throws InvalidUserException, UserNotFoundException, DataBaseFailException, InvalidClientException {
        long notExistingId = 1L;

        given(clientRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        Client clientToBeUpdated = TestDataBuilder.buildValidClientToUpdate();
        clientToBeUpdated.setId(notExistingId);

        User userToBeUpdated = new User(clientToBeUpdated.getId(), clientToBeUpdated.getName(), UserType.CLIENT, clientToBeUpdated.getUserName(), clientToBeUpdated.getPassword());

        doThrow(UserNotFoundException.class).when(userService).updateUser(userToBeUpdated);

        AtomicReference<Client> updatedClient = new AtomicReference<>();
        Assertions.assertThrows(UserNotFoundException.class, () -> updatedClient.set(clientService.updateClient(clientToBeUpdated)));

        Assertions.assertNull(updatedClient.get());

        then(clientRepository)
                .should(never())
                .saveIfValid(clientToBeUpdated);

    }

    @Test
    public void givenClientObject_whenDeleteClientById_thenNothing() {
        Client client = TestDataBuilder.buildValidClient1();
        client.setId(1L);

        given(clientRepository.findById(client.getId()))
                .willReturn(Optional.of(client));


        doNothing().when(clientRepository).deleteById(client.getId());

        Assertions.assertDoesNotThrow(() -> clientService.deleteClientById(client.getId()));


        then(clientRepository)
                .should()
                .deleteById(client.getId());

    }

    @Test
    public void givenNotExistingId_whenDeleteClientById_thenThrowException() {
        long notExistingId = 100L;

        given(clientRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        Assertions.assertThrows(ClientNotFoundException.class, () -> clientService.deleteClientById(notExistingId));

        then(clientRepository)
                .should(never())
                .deleteById(notExistingId);

    }
}
