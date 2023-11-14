package com.hospital.Exception;

public class AppointmentAlreadyExistsException extends Exception{
    public AppointmentAlreadyExistsException(String message) {
        super(message);
    }
}
