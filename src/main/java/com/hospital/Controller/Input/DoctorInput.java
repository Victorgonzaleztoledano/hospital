package com.hospital.Controller.Input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorInput extends EmployeeInput {
    @Min(value = 0, message = "Experience can not be negative")
    @Max(value = 50, message = "Experience must be consistent")
    private int experience;
}