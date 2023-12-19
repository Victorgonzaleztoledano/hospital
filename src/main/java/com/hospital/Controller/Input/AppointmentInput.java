package com.hospital.Controller.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentInput {

    @NotNull(message = "Day can not be null")
    private LocalDate dateAppointment;

    @NotNull(message = "Time can not be null")
    private LocalTime timeAppointment;

    @NotNull(message = "Dni can not be null")
    @NotBlank(message = "Dni can not be empty")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid dni format. Must be 8 numbers followed by a letter")
    private String dniPatient;

    @NotNull
    @NotBlank
    private String codeEmployee;
}