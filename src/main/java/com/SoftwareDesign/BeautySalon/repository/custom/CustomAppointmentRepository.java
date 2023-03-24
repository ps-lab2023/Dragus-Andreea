package com.SoftwareDesign.BeautySalon.repository.custom;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidAppointmentException;

import java.util.Optional;

public interface CustomAppointmentRepository {
    Optional<Appointment> saveIfValid(Appointment appointment) throws InvalidAppointmentException;
}
