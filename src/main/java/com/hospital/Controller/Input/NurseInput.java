package com.hospital.Controller.Input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;

public class NurseInput extends EmployeeInput {
    public NurseInput(@NotNull(message = "Dni can not be null") @NotBlank(message = "Dni can not be empty") @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid DNI format. Must be 8 numbers followed by a letter") String dni, @NotNull(message = "Address can not be null") @NotBlank(message = "Address can not be empty") String address, @NotNull(message = "Name can not be null") @NotBlank(message = "Name can not be empty") @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers") String name, @NotNull @NotBlank String code, @NotNull LocalTime workingTime, @NotNull LocalTime endWorkingTime) {
        super(dni, address, name, workingTime, endWorkingTime);
    }
}