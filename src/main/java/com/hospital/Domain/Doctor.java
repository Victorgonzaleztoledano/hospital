package com.hospital.Domain;
import com.hospital.Controller.Input.DoctorInput;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "doctors")
@DiscriminatorValue("doctors")
public class Doctor extends Employee{
    @Column(name = "experience")
    @Min(value = 0, message = "Experience can not be negative")
    @Max(value = 50, message = "Experience must be consistent")
    private int experience;

    public Doctor(String dni, String address, String name, LocalTime workingTime, LocalTime endWorkingTime, int experience) {
        super(dni, address, name, workingTime, endWorkingTime);
        this.experience = experience;
    }

    public static Doctor getDoctor(DoctorInput doctorInput) {
        return new Doctor(doctorInput.getDni(), doctorInput.getAddress(), doctorInput.getName(), doctorInput.getWorkingTime(), doctorInput.getEndWorkingTime(),
                doctorInput.getExperience());
    }
}