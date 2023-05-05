package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.model.User;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidBeautyServiceException;
import com.SoftwareDesign.BeautySalon.repository.BeautyServiceRepository;
import com.SoftwareDesign.BeautySalon.service.BeautyServiceService;
import com.SoftwareDesign.BeautySalon.service.exception.BeautyServiceNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class BeautyServiceServiceImpl implements BeautyServiceService {
    @Autowired
    private BeautyServiceRepository beautyServiceRepository;

    public BeautyServiceServiceImpl(BeautyServiceRepository beautyServiceRepository) {
        this.beautyServiceRepository = beautyServiceRepository;
    }

    @Override
    public BeautyService getBeautyServiceById(Long id) throws BeautyServiceNotFoundException {
        Optional<BeautyService> beautyServiceOptional = beautyServiceRepository.findBeautyServiceById(id);

        if(beautyServiceOptional.isPresent()) {
            return beautyServiceOptional.get();
        } else {
            throw new BeautyServiceNotFoundException("Beauty Service not found with id = " + id);
        }
    }

    @Override
    public BeautyService getBeautyServiceByName(String name) throws BeautyServiceNotFoundException {
        Optional<BeautyService> beautyServiceOptional = beautyServiceRepository.findBeautyServiceByName(name);

        if(beautyServiceOptional.isPresent()) {
            return beautyServiceOptional.get();
        } else {
            throw new BeautyServiceNotFoundException("Beauty Service not found with name = " + name);
        }
    }

    @Override
    public List<BeautyService> getBeautyServicesByAppointmentId(Long appointmentId) {
        return null;
    }

    @Override
    public List<BeautyService> getBeautyServicesByEmployeeType(EmployeeType employeeType) throws BeautyServiceNotFoundException {
        List<BeautyService> beautyServices = beautyServiceRepository.findBeautyServicesByEmployeeType(employeeType);

        if(!CollectionUtils.isEmpty(beautyServices)) {
            return beautyServices;
        } else {
            throw new BeautyServiceNotFoundException("Beauty Services not found with employee type = " + employeeType);
        }
    }

    @Override
    public List<BeautyService> getAllBeautyServices() throws BeautyServiceNotFoundException {
        List<BeautyService> beautyServices = beautyServiceRepository.findAll();

        if(!CollectionUtils.isEmpty(beautyServices)) {
            return beautyServices;
        } else {
            throw new BeautyServiceNotFoundException("Beauty Services not found in DB = ");
        }
    }

    @Override
    public BeautyService addBeautyService(BeautyService beautyService) throws InvalidBeautyServiceException, DataBaseFailException {
        Optional<BeautyService> beautyServiceWithSameName = this.beautyServiceRepository.findBeautyServiceByName(beautyService.getName());
        if(beautyServiceWithSameName.isEmpty()) {
            Optional<BeautyService> beautyService1 = this.beautyServiceRepository.saveIfValid(beautyService);

            if(beautyService1.isPresent()) {
                return beautyService1.get();
            } else {
                throw new DataBaseFailException("Failed to load beauty service in DB");
            }
        } else {
            throw new DataBaseFailException("BeautyService with this name already exists in DB");
        }
    }

    @Override
    public BeautyService updateBeautyService(BeautyService beautyService) throws BeautyServiceNotFoundException, InvalidBeautyServiceException, DataBaseFailException {
        Optional<BeautyService> foundBeautyService = this.beautyServiceRepository.findBeautyServiceById(beautyService.getId());

        if(foundBeautyService.isPresent()) {

            BeautyService beautyService1 = new BeautyService(foundBeautyService.get().getId(), beautyService.getName(), beautyService.getPrice(), beautyService.getEmployeeType());
            Optional<BeautyService> updatedBeautyService = this.beautyServiceRepository.saveIfValid(beautyService1);

            if(updatedBeautyService.isPresent()) {
                return updatedBeautyService.get();
            } else {
                throw new DataBaseFailException("Failed to update beauty service");
            }
        } else {
            throw new BeautyServiceNotFoundException("Could not update beauty Service because it was not found in DB");
        }
    }

    @Override
    public void deleteBeautyService(Long id) throws BeautyServiceNotFoundException {
        Optional<BeautyService> beautyServiceOptional = this.beautyServiceRepository.findById(id);
        if(beautyServiceOptional.isPresent()) {
            beautyServiceRepository.deleteById(id);
        } else {
            throw new BeautyServiceNotFoundException("Could not delete beauty service because it was not found in User DB");
        }
    }
}
