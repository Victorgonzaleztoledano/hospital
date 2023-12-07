package com.hospital.Exception.GlobalHandler;

import com.hospital.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return errors;
    }

    @ExceptionHandler({EmployeeNotExistsException.class, NurseNotExistsException.class, DoctorNotExistsException.class, PatientNotFoundException.class, EmptyListException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleNotFoundException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return errors;
    }

    @ExceptionHandler({AppointmentAlreadyExistsException.class, DniAlreadyExistsException.class, DoctorAlreadyExistsException.class, NurseAlreadyExistsException.class, PatientAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, String> handleAlreadyExistsException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return errors;
    }

    @ExceptionHandler({WrongDateException.class, WrongTimeException.class, NotAvalibleException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleWrongException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return errors;
    }
}