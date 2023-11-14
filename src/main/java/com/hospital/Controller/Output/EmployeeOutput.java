package com.hospital.Controller.Output;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", code=" + code;
    }
}