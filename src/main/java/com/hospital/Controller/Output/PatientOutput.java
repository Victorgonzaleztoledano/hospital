package com.hospital.Controller.Output;

import com.hospital.Domain.Patient;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PatientOutput {
    @NotNull(message = "Dni can not be null")
    @NotBlank(message = "Dni can not be empty")
    @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid DNI format. Must be 8 numbers followed by a letter")
    private String dni;

    @NotNull(message = "Adress can not be null")
    @NotBlank(message = "Adress can not be empty")
    private String adress;

    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be empty")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers")
    private String name;

    public static PatientOutput getPatientOutput(Patient patient){
        return new PatientOutput(patient.getDni(), patient.getAddress(), patient.getName());
    }
}