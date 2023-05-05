package com.SoftwareDesign.BeautySalon.repository.custom;

import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;

import java.util.Optional;

public interface CustomClientRepository  {
    Optional<Client> saveIfValid(Client client) throws InvalidClientException, InvalidUserException;
}
