package com.SoftwareDesign.BeautySalon.model.validation.exception;

public class InvalidBeautyServiceException extends Exception{
    public InvalidBeautyServiceException() {
    }

    public InvalidBeautyServiceException(String message) {
        super(message);
    }
}
