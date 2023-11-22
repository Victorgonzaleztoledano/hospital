package com.hospital.Domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("employees")
public abstract class Employee {

    @NotNull(message = "Dni can not be null")
    @NotBlank(message = "Dni can not be empty")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid dni format. Must be 8 numbers followed by a letter")
    @Id
    private String dni;

    @NotNull(message = "Address can not be null")
    @NotBlank(message = "Address can not be empty")
    private String address;

    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be empty")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers")
    private String name;
    //Si coloco excepciones para code da conflicto
    private String code = String.valueOf(UUID.randomUUID());
    @NotNull
    private LocalTime workingTime;
    @NotNull
    private LocalTime endWorkingTime;

    public Employee(String dni, String address, String name, LocalTime workingTime, LocalTime endWorkingTime) {
        this.dni = dni;
        this.address = address;
        this.name = name;
        this.workingTime = workingTime;
        this.endWorkingTime = endWorkingTime;
    }
}