package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.EmployeeType;
import com.SoftwareDesign.BeautySalon.service.exception.BeautyServiceNotFoundException;

import java.util.List;


public interface BeautyServiceService {
    BeautyService getBeautyServiceById(Long id) throws BeautyServiceNotFoundException;
    BeautyService getBeautyServiceByName(String name) throws BeautyServiceNotFoundException;
    List<BeautyService> getBeautyServicesByAppointmentId(Long appointmentId);
    List<BeautyService> getBeautyServicesByEmployeeType(EmployeeType employeeType) throws BeautyServiceNotFoundException;
    BeautyService getAllBeautyServices();
    BeautyService addBeautyService(BeautyService beautyService);
    BeautyService updateBeautyService(BeautyService beautyService);
    BeautyService deleteBeautyService(Long id);
}
