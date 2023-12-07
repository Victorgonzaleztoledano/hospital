package com.hospital.Domain;

import com.hospital.Controller.Input.AppointmentInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Table(name = "appointments")
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Appointment {
    @Id
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
    private String employeeCode;

    public Appointment(LocalDate dateAppointment, LocalTime timeAppointment, String dniPatient, String employeeCode) {
        this.dateAppointment = dateAppointment;
        this.timeAppointment = timeAppointment;
        this.dniPatient = dniPatient;
        this.employeeCode = employeeCode;
    }

    public static Appointment getAppointment(AppointmentInput appointmentInput){
        return new Appointment(appointmentInput.getDateAppointment(),
                appointmentInput.getTimeAppointment(),
                appointmentInput.getDniPatient(), appointmentInput.getCodeEmployee());
    }
}