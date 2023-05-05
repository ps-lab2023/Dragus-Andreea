package com.SoftwareDesign.BeautySalon.repository.custom.impl;

import com.SoftwareDesign.BeautySalon.model.*;
import com.SoftwareDesign.BeautySalon.model.validation.AppointmentValidator;
import com.SoftwareDesign.BeautySalon.model.validation.ClientValidator;
import com.SoftwareDesign.BeautySalon.model.validation.EmployeeValidator;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;
import com.SoftwareDesign.BeautySalon.repository.BeautyServiceRepository;
import com.SoftwareDesign.BeautySalon.repository.ClientRepository;
import com.SoftwareDesign.BeautySalon.repository.EmployeeRepository;
import com.SoftwareDesign.BeautySalon.repository.UserRepository;
import com.SoftwareDesign.BeautySalon.repository.custom.CustomAppointmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.DataBinder;

import java.util.Optional;

@Repository
public class CustomAppointmentRepositoryImpl implements CustomAppointmentRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BeautyServiceRepository beautyServiceRepository;


    @Override
    @Transactional
    public Optional<Appointment> saveIfValid(Appointment appointment) throws InvalidAppointmentException {
        DataBinder dataBinder = new DataBinder(appointment);
        dataBinder.addValidators(new AppointmentValidator(new ClientValidator(), new EmployeeValidator()));
        dataBinder.validate();

        if(!dataBinder.getBindingResult().hasErrors()) {
            Optional<User> clientUser = userRepository.findById(appointment.getClient().getId());
            Optional<User> employeeUser = userRepository.findById(appointment.getEmployee().getId());
            if(clientUser.isPresent() && employeeUser.isPresent()) {
                Optional<Client> client = clientRepository.findById(appointment.getClient().getId());
                Optional<Employee> employee = employeeRepository.findById(appointment.getEmployee().getId());

                if(client.isPresent() && employee.isPresent()) {
                    if(appointment.getId() == null) {
                        entityManager.persist(appointment);
                    } else {
                        entityManager.merge(appointment);
                    }
                    return Optional.of(appointment);
                }
                else {
                    throw new InvalidAppointmentException("Client or Employee not found in client/employee data base");
                }
            } else {
                throw new InvalidAppointmentException("Client or Employee not found in user data base");
            }

        } else {
            throw new InvalidAppointmentException(dataBinder.getBindingResult().toString());
        }
    }
}
