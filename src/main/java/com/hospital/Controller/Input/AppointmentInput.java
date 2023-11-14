package com.hospital.Controller.Input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentInput {
    @NotNull
    @NotBlank
    private String id = String.valueOf(UUID.randomUUID());

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

    public AppointmentInput(LocalDate dateAppointment, LocalTime timeAppointment, String dniPatient, String codeEmployee) {
        this.dateAppointment = dateAppointment;
        this.timeAppointment = timeAppointment;
        this.dniPatient = dniPatient;
        this.codeEmployee = codeEmployee;
    }
}
