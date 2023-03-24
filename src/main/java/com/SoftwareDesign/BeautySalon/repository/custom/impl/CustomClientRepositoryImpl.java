package com.SoftwareDesign.BeautySalon.repository.custom.impl;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.UserType;
import com.SoftwareDesign.BeautySalon.model.validation.ClientValidator;
import com.SoftwareDesign.BeautySalon.model.validation.UserValidator;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;

import java.util.Optional;

@Repository
public class CustomClientRepositoryImpl implements CustomClientRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Optional<Client> saveIfValid(Client client) throws InvalidClientException, InvalidUserException {
        DataBinder dataBinder = new DataBinder(client);
        dataBinder.addValidators(new ClientValidator());
        dataBinder.validate();

        if(!dataBinder.getBindingResult().hasErrors()) {
            if(client.getId() == null) {
                User user = new User(client.getName(), UserType.CLIENT, client.getUserName(), client.getPassword());
                userRepository.saveIfValid(user);
                Client client2 = new Client(user.getId(), client.getName(), client.getUserName(), client.getPassword(), client.getLoyaltyPoints());
                entityManager.persist(client2);
                return Optional.of(client2);
            } else {
                entityManager.merge(client);
                return Optional.of(client);
            }

        } else {
            throw new InvalidClientException();
        }
    }
}
