package com.SoftwareDesign.BeautySalon.service.exception;

public class EmployeeNotFoundException extends Exception{
    public EmployeeNotFoundException(String message) {
        super("Employee not found exception");
    }
}
