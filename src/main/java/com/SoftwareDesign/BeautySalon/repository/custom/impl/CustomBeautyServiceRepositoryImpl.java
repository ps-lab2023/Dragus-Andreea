package com.SoftwareDesign.BeautySalon.repository.custom.impl;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.validation.BeautyServiceValidator;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidBeautyServiceException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomBeautyServiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.validation.DataBinder;

import java.util.Optional;

public class CustomBeautyServiceRepositoryImpl implements CustomBeautyServiceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Optional<BeautyService> saveIfValid(BeautyService beautyService) throws InvalidBeautyServiceException {
        DataBinder dataBinder = new DataBinder(beautyService);
        dataBinder.addValidators(new BeautyServiceValidator());
        dataBinder.validate();

        if(!dataBinder.getBindingResult().hasErrors()) {
            if(beautyService.getId() == null ) {
                entityManager.persist(beautyService);
            } else {
                entityManager.merge(beautyService);
            }
            BeautyService savedBeautyService = new BeautyService(beautyService.getId(), beautyService.getName(), beautyService.getPrice(), beautyService.getEmployeeType());

            return Optional.ofNullable(savedBeautyService);
        } else {
            throw new InvalidBeautyServiceException("Could not save beauty service in DB because it is invalid");
        }
    }
}
