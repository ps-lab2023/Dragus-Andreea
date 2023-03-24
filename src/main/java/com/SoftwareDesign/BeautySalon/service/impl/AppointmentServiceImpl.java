package com.SoftwareDesign.BeautySalon.service.impl;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.repository.AppointmentRepository;;
import com.SoftwareDesign.BeautySalon.repository.BeautyServiceRepository;
import com.SoftwareDesign.BeautySalon.service.AppointmentService;
import com.SoftwareDesign.BeautySalon.service.exception.AppointmentNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
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

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  BeautyServiceRepository beautyServiceRepository) {
        this.appointmentRepository = appointmentRepository;
        this.beautyServiceRepository = beautyServiceRepository;
    }

    @Override
    public Appointment addAppointment(Appointment appointment) throws DataBaseFailException, InvalidAppointmentException {
        Optional<Appointment> existingAppointmentEmployee = this.appointmentRepository.findByEmployeeAndDateTime(appointment.getEmployee(), appointment.getDateTime());
        Optional<Appointment> existingAppointmentClient = this.appointmentRepository.findByClientAndDateTime(appointment.getClient(), appointment.getDateTime());

        if(existingAppointmentEmployee.isEmpty() && existingAppointmentClient.isEmpty()) {
            Optional<Appointment> appointmentOptional = this.appointmentRepository.saveIfValid(appointment);
            if (appointmentOptional.isPresent()) {
                appointmentOptional.get().getClient().addAppointment(appointment);
                appointmentOptional.get().getEmployee().addAppointment(appointment);
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
    public Appointment getAppointmentByEmployeeAndDateTime(Employee employee, LocalDateTime dateTime) throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findByEmployeeAndDateTime(employee, dateTime);
        if(appointmentOptional.isPresent()) {
            return appointmentOptional.get();
        } else {
            throw new AppointmentNotFoundException("Appointment not found by employee = " + employee + " and " + "dateTime = "+ dateTime);
        }
    }

    @Override
    public Appointment getAppointmentByClientAndDateTime(Client client, LocalDateTime dateTime) throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findByClientAndDateTime(client, dateTime);
        if(appointmentOptional.isPresent()) {
            return appointmentOptional.get();
        } else {
            throw new AppointmentNotFoundException("Appointment not found by employee = " + client + " and " + "dateTime = "+ dateTime);
        }
    }


    @Override
    public List<Appointment> getAllAppointmentsByEmployee(Employee employee) throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAllByEmployee(employee);

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointment not found by employee = "+ employee);
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsByClient(Client client) throws AppointmentNotFoundException {
        List<Appointment> appointmentsOptional = this.appointmentRepository.findAllByClient(client);

        if(!CollectionUtils.isEmpty(appointmentsOptional)) {
            return appointmentsOptional;
        } else {
            throw new AppointmentNotFoundException("Appointment not found by client = "+ client);
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
    public Appointment updateAppointment(Appointment appointment) throws InvalidAppointmentException, DataBaseFailException, AppointmentNotFoundException {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointment.getId());
        if(appointmentOptional.isPresent()) {
            appointment.setClient(appointmentOptional.get().getClient());
            appointment.setEmployee(appointmentOptional.get().getEmployee());

            if(appointment.getDateTime() == null) {
                appointment.setDateTime(appointmentOptional.get().getDateTime());
            }

            if(appointment.getBeautyServices() == null || CollectionUtils.isEmpty(appointment.getBeautyServices())) {
                appointment.setBeautyServices(appointmentOptional.get().getBeautyServices());
                appointment.computeTotalPrice();
            }
            return addAppointment(appointment);

        } else {
            throw new AppointmentNotFoundException("Could not update appointment because it was not found in DB");
        }
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
