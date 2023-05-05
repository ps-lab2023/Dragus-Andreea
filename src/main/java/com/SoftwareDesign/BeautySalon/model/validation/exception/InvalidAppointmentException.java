package com.SoftwareDesign.BeautySalon.model.validation.exception;

public class InvalidAppointmentException extends Exception{
    public InvalidAppointmentException() {
        super();
    }

    public InvalidAppointmentException(String message) {
        super(message);
    }
}
