package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.repository.BeautyServiceRepository;
import com.SoftwareDesign.BeautySalon.service.BeautyServiceService;
import com.SoftwareDesign.BeautySalon.service.exception.BeautyServiceNotFoundException;
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
    public BeautyService getAllBeautyServices() {
        return null;
    }

    @Override
    public BeautyService addBeautyService(BeautyService beautyService) {
        return null;
    }

    @Override
    public BeautyService updateBeautyService(BeautyService beautyService) {
        return null;
    }

    @Override
    public BeautyService deleteBeautyService(Long id) {
        return null;
    }
}
