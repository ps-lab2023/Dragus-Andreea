package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.service.exception.AppointmentNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AppointmentService {
    Appointment addAppointment(Appointment appointment) throws DataBaseFailException, InvalidAppointmentException;
    Appointment getAppointmentById(Long id) throws AppointmentNotFoundException;
    Appointment getAppointmentByEmployeeAndDateTime(Employee employee, LocalDateTime dateTime) throws AppointmentNotFoundException;
    Appointment getAppointmentByClientAndDateTime(Client client, LocalDateTime dateTime) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByEmployee(Employee employee) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByClient(Client client) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByBeautyServicesContaining(BeautyService beautyService) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByTotalPriceGreaterThan(BigDecimal totalPrice) throws AppointmentNotFoundException;
    Appointment updateAppointment(Appointment appointment) throws InvalidAppointmentException, DataBaseFailException, AppointmentNotFoundException;
    void deleteAppointmentById(Long id) throws AppointmentNotFoundException;
}
