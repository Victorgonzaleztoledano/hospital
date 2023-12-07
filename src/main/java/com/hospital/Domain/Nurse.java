package com.hospital.Domain;

import com.hospital.Controller.Input.NurseInput;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nurses")
@DiscriminatorValue("nurses")
public class Nurse extends Employee{

    public Nurse(String dni, String address, String name, LocalTime workingTime, LocalTime endWorkingTime) {
        super(dni, address, name, workingTime, endWorkingTime);
    }

    public static Nurse getNurse(NurseInput nurseInput) {
        return new Nurse(nurseInput.getDni(), nurseInput.getAddress(), nurseInput.getName(),nurseInput.getWorkingTime(), nurseInput.getEndWorkingTime());
    }
}