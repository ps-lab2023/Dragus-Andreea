package com.SoftwareDesign.BeautySalon.service;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.BeautyService;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.exception.AppointmentNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AppointmentService {
    Appointment addAppointment(Appointment appointment) throws DataBaseFailException, InvalidAppointmentException, UserNotFoundException, ClientNotFoundException, InvalidClientException, InvalidUserException;
    Appointment getAppointmentById(Long id) throws AppointmentNotFoundException;
    Appointment getAppointmentByEmployeeNameAndDateTime(String name, LocalDateTime dateTime) throws AppointmentNotFoundException;
    Appointment getAppointmentByClientNameAndDateTime(String name, LocalDateTime dateTime) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByEmployeeId(Long id) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByClientId(Long id) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByBeautyServicesContaining(BeautyService beautyService) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointmentsByTotalPriceGreaterThan(BigDecimal totalPrice) throws AppointmentNotFoundException;
    List<Appointment> getAllAppointments();
    Appointment updateAppointment(Appointment appointment) throws InvalidAppointmentException, DataBaseFailException, AppointmentNotFoundException, UserNotFoundException, ClientNotFoundException, InvalidClientException, InvalidUserException;
    void deleteAppointmentById(Long id) throws AppointmentNotFoundException;
}
