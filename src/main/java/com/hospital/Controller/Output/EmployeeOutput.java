package com.hospital.Controller.Output;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class EmployeeOutput {
    @Id
    @NotNull(message = "Dni can not be null")
    @NotBlank(message = "Dni can not be empty")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid DNI format. Must be 8 numbers followed by a letter")
    private String dni;

    @NotNull(message = "Address can not be null")
    @NotBlank(message = "Address can not be empty")
    private String address;

    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be empty")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers")
    private String name;

    @NotNull
    @NotBlank
    private String code;

    @NotNull
    private LocalTime workingTime;
    @NotNull
    private LocalTime endWorkingTime;
}