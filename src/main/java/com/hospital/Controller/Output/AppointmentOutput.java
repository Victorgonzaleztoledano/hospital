package com.hospital.Controller.Output;

import com.hospital.Domain.Appointment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentOutput {
    @NotNull
    private String id;

    @NotNull(message = "Day can not be null")
    private LocalDate dateAppointment;

    @NotNull(message = "Time can not be null")
    private LocalTime timeAppointment;

    @NotNull(message = "Dni can not be null")
    @NotBlank(message = "Dni can not be empty")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid dni format. Must be 8 numbers followed by a letter")
    private String dniPatient;

    @Min(value = 0, message = "Code can not be negative")
    private String codeEmployee;


    public static AppointmentOutput getAppointmentOutput(Appointment appointment){
        return new AppointmentOutput(appointment.getId(), appointment.getDateAppointment(),
                appointment.getTimeAppointment(), appointment.getDniPatient(), appointment.getEmployeeCode());
    }
}
