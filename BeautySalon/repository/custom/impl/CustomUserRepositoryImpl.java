package com.SoftwareDesign.BeautySalon.repository.custom.impl;

import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.UserValidator;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomUserRepository;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;

import java.util.Optional;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Optional<User> saveIfValid(User user) throws InvalidUserException {
        DataBinder dataBinder = new DataBinder(user);
        dataBinder.addValidators(new UserValidator());
        dataBinder.validate();

        if(!dataBinder.getBindingResult().hasErrors()) {
            if(user.getId() == null ) {
                entityManager.persist(user);
            } else {
                entityManager.merge(user);
            }
            User savedUser = new User(user.getId(), user.getName(), user.getUserType(), user.getUserName(), user.getPassword());

            return Optional.ofNullable(savedUser);
        } else {
            throw new InvalidUserException();
        }
    }



}
