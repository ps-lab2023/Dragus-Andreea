package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.repository.AppointmentRepository;;
import com.SoftwareDesign.BeautySalon.repository.BeautyServiceRepository;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.exception.AppointmentNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    private final BeautyServiceRepository beautyServiceRepository;
    @Autowired
    private ClientService clientService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  BeautyServiceRepository beautyServiceRepository,
                                  ClientService clientService) {
        this.appointmentRepository = appointmentRepository;
        this.beautyServiceRepository = beautyServiceRepository;
        this.clientService = clientService;
    }

    @Override
    public Appointment addAppointment(Appointment appointment) throws DataBaseFailException, InvalidAppointmentException, UserNotFoundException, ClientNotFoundException, InvalidClientException, InvalidUserException {
        Optional<Appointment> existingAppointmentEmployee = this.appointmentRepository.findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime());
        Optional<Appointment> existingAppointmentClient = this.appointmentRepository.findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime());

        if(existingAppointmentEmployee.isEmpty() && existingAppointmentClient.isEmpty()) {
            Optional<Appointment> appointmentOptional = this.appointmentRepository.saveIfValid(appointment);
            if (appointmentOptional.isPresent()) {
                appointmentOptional.get().getClient().addAppointment(appointment);
                appointmentOptional.get().getEmployee().addAppointment(appointment);
                //Client client = appointmentOptional.get().getClient();
                //clientService.updateClient(client);
                return appointmentOptional.get();
            } else {
                throw new DataBaseFailException("Failed to load appointment in DB");
            }
        } else {
            throw new InvalidAppointmentException("TimeSlot already occupied for appointment");
        }
    }

    @Override
    public Appointment getAppointmentById(Long id) throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findById(id);

        if(appointmentOptional.isPresent()) {
            return appointmentOptional.get();
        } else {
            throw new AppointmentNotFoundException("Appointment not found by id = "+ id);
        }
    }

    @Override
    public Appointment getAppointmentByEmployeeNameAndDateTime(String name, LocalDateTime dateTime) throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findByEmployeeNameAndDateTime(name, dateTime);
        if(appointmentOptional.isPresent()) {
            return appointmentOptional.get();
        } else {
           throw new AppointmentNotFoundException("Appointment not found by employee name = " + name + " and " + "dateTime = "+ dateTime);
        }
    }

    @Override
    public Appointment getAppointmentByClientNameAndDateTime(String name, LocalDateTime dateTime) throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findByClientNameAndDateTime(name, dateTime);
        if(appointmentOptional.isPresent()) {
            return appointmentOptional.get();
        } else {
            throw new AppointmentNotFoundException("Appointment not found by client name = " + name + " and " + "dateTime = "+ dateTime);
        }
    }


    @Override
    public List<Appointment> getAllAppointmentsByEmployeeId(Long id) throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAllByEmployeeId(id);

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointment not found by employee with id = "+ id);
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsByClientId(Long id) throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAllByClientId(id);

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointment not found by client with id = "+ id);
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsByBeautyServicesContaining(BeautyService beautyService) throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAllByBeautyServicesContaining(beautyService);

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointment not found by containing beauty service = "+ beautyService);
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsByTotalPriceGreaterThan(BigDecimal totalPrice) throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAllByTotalPriceGreaterThan(totalPrice);

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointment not found by price greater than = "+ totalPrice);
        }
    }

    @Override
    public List<Appointment> getAllAppointments() throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAll();

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointments not found");
        }
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) throws InvalidAppointmentException, DataBaseFailException, AppointmentNotFoundException, UserNotFoundException, ClientNotFoundException, InvalidClientException, InvalidUserException {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointment.getId());
        if(appointmentOptional.isPresent()) {
            appointment.setClient(appointmentOptional.get().getClient());
            appointment.setEmployee(appointmentOptional.get().getEmployee());

            if(appointment.getDateTime() == null) {
                appointment.setDateTime(appointmentOptional.get().getDateTime());
            }

            if(appointment.getBeautyServices() == null || CollectionUtils.isEmpty(appointment.getBeautyServices())) {
                appointment.setBeautyServices(appointmentOptional.get().getBeautyServices());
                System.out.println(appointmentOptional.get().getBeautyServices());
                appointment.computeTotalPrice();
                System.out.println(appointment.getTotalPrice());
            }
            Optional<Appointment> existingAppointmentEmployee = this.appointmentRepository.findByEmployeeNameAndDateTime(appointment.getEmployee().getName(), appointment.getDateTime());
            Optional<Appointment> existingAppointmentClient = this.appointmentRepository.findByClientNameAndDateTime(appointment.getClient().getName(), appointment.getDateTime());

            if(existingAppointmentEmployee.isEmpty() && existingAppointmentClient.isEmpty()) {
                Optional<Appointment> appointmentSavedOptional = this.appointmentRepository.saveIfValid(appointment);
                if(appointmentSavedOptional.isPresent()) {
                    return appointmentSavedOptional.get();
                } else {
                    throw new AppointmentNotFoundException("Could not update appointment because it was not found in DB");
                }

        } else {
            throw new AppointmentNotFoundException("Could not update appointment because it was not found in DB");
        }
    }
        throw new AppointmentNotFoundException("Could not update appointment because it was not found in DB");
    }


    @Override
    public void deleteAppointmentById(Long id) throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if(appointmentOptional.isPresent()) {
            appointmentRepository.deleteById(id);
        } else {
            throw new AppointmentNotFoundException("Could not delete appointment because it was not found in DB");
        }
    }
}
