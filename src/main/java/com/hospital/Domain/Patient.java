package com.hospital.Domain;


import com.hospital.Controller.Input.PatientInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Table(name = "patients")
@Entity
@NoArgsConstructor
public class Patient extends Person{

    public Patient(@NotNull(message = "Dni can not be null") @NotBlank(message = "Dni can not be empty")
                   @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid dni format. Must be 8 numbers followed by a letter") String dni,
                   @NotNull(message = "Address can not be null") @NotBlank(message = "Address can not be empty") String address,
                   @NotNull(message = "Name can not be null") @NotBlank(message = "Name can not be empty")
                   @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers") String name) {
        super(dni, address, name);
    }

    public static Patient getPatient(PatientInput patientInput){
        return new Patient(patientInput.getDni(), patientInput.getAddress(), patientInput.getName());
    }
}
