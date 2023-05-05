package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidBeautyServiceException;
import com.SoftwareDesign.BeautySalon.service.exception.BeautyServiceNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;

import java.util.List;


public interface BeautyServiceService {
    BeautyService getBeautyServiceById(Long id) throws BeautyServiceNotFoundException;
    BeautyService getBeautyServiceByName(String name) throws BeautyServiceNotFoundException;
    List<BeautyService> getBeautyServicesByAppointmentId(Long appointmentId);
    List<BeautyService> getBeautyServicesByEmployeeType(EmployeeType employeeType) throws BeautyServiceNotFoundException;
    List<BeautyService> getAllBeautyServices() throws BeautyServiceNotFoundException;
    BeautyService addBeautyService(BeautyService beautyService) throws InvalidBeautyServiceException, DataBaseFailException;
    BeautyService updateBeautyService(BeautyService beautyService) throws BeautyServiceNotFoundException, InvalidBeautyServiceException, DataBaseFailException;
    void deleteBeautyService(Long id) throws BeautyServiceNotFoundException;
}
