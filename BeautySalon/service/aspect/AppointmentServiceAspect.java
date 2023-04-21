package com.SoftwareDesign.BeautySalon.service.aspect;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidClientException;
import com.SoftwareDesign.BeautySalon.model.validation.exception.InvalidUserException;
import com.SoftwareDesign.BeautySalon.service.ClientService;
import com.SoftwareDesign.BeautySalon.service.exception.ClientNotFoundException;
import com.SoftwareDesign.BeautySalon.service.exception.DataBaseFailException;
import com.SoftwareDesign.BeautySalon.service.exception.UserNotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AppointmentServiceAspect {
    @Autowired
    private ClientService clientService;

    @Around(value = "execution(* com.SoftwareDesign.BeautySalon.service.AppointmentService.addAppointment(..))")
    public Object after(ProceedingJoinPoint proceedingJoinPoint) throws UserNotFoundException, ClientNotFoundException, DataBaseFailException, InvalidClientException, InvalidUserException {
        Object appointmentObj = null;

        try {
            appointmentObj = proceedingJoinPoint.proceed();
            Appointment appointment = (Appointment) appointmentObj;
            Client clientUpdatedLoyaltyPoints = clientService.updateClient(appointment.getClient());
            System.out.println("UPDATED "+ clientUpdatedLoyaltyPoints);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return appointmentObj;

    }
}
