package com.SoftwareDesign.BeautySalon.model.validation;

import com.SoftwareDesign.BeautySalon.model.Appointment;
import com.SoftwareDesign.BeautySalon.model.Client;
import com.SoftwareDesign.BeautySalon.model.Employee;
import org.hibernate.Hibernate;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

public class AppointmentValidator implements Validator {
    private final Validator clientValidator;
    private final Validator employeeValidator;

    public AppointmentValidator(Validator clientValidator, Validator employeeValidator) {
        if (clientValidator == null) {
            throw new IllegalArgumentException("The supplied [clientValidator] is " +
                    "required and must not be null.");
        }

        if (employeeValidator == null) {
            throw new IllegalArgumentException("The supplied [employeeValidator] is " +
                    "required and must not be null.");
        }

        if (!clientValidator.supports(Client.class)) {
            throw new IllegalArgumentException("The supplied [clientValidator] must " +
                    "support the validation of [Client] instances.");
        }

        if (!employeeValidator.supports(Employee.class)) {
            throw new IllegalArgumentException("The supplied [employeeValidator] must " +
                    "support the validation of [Employee] instances.");
        }

        this.clientValidator = clientValidator;
        this.employeeValidator = employeeValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Appointment.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "client", "client.empty");
        ValidationUtils.rejectIfEmpty(errors, "employee", "employee.empty");
        ValidationUtils.rejectIfEmpty(errors, "dateTime", "dateTime.empty");

        Appointment appointment = (Appointment) target;

        if(appointment.getDateTime().isBefore(LocalDateTime.now())) {
            errors.rejectValue("dateTime", "date.is.in.past");
        }

        if(appointment.getDateTime().getHour() > 18 || appointment.getDateTime().getHour() < 8) {
            errors.rejectValue("dateTime", "time.invalid.Hour");
        }
        /*
        if(appointment.getDateTime().getMinute() != 0 || appointment.getDateTime().getMinute() != 30) {
            errors.rejectValue("dateTime", "time.invalid.Min");
        } */

        try {
            errors.pushNestedPath("client");
            ValidationUtils.invokeValidator(this.clientValidator, Hibernate.unproxy(appointment.getClient()), errors);

        } finally {
            errors.popNestedPath();
        }

        try{
            errors.pushNestedPath("employee");
            ValidationUtils.invokeValidator(this.employeeValidator, Hibernate.unproxy(appointment.getEmployee()), errors);
        } finally {
            errors.popNestedPath();
        }

    }
}
