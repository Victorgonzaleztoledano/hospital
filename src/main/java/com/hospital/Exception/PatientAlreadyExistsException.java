package com.hospital.Exception;

public class PatientAlreadyExistsException extends Exception {
    public PatientAlreadyExistsException(String message) {
        super(message);
    }
}