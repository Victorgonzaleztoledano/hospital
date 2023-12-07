package com.hospital.Controller.Output;

import com.hospital.Domain.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DoctorOutput extends EmployeeOutput {
    @Min(value = 0, message = "Experience can not be negative")
    @Max(value = 50, message = "Experience must be consistent")
    private int experience;

    public DoctorOutput(@NotNull(message = "Dni can not be null") @NotBlank(message = "Dni can not be empty") @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid DNI format. Must be 8 numbers followed by a letter") String dni, @NotNull(message = "Address can not be null") @NotBlank(message = "Address can not be empty") String address, @NotNull(message = "Name can not be null") @NotBlank(message = "Name can not be empty") @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers") String name, @Min(value = 0, message = "Code can not be negative") String code, LocalTime workingTime, LocalTime endWorkingTime, int experience) {
        super(dni, address, name, code, workingTime, endWorkingTime);
        this.experience = experience;
    }

    public static DoctorOutput getDoctorOutput(Doctor doctor){
        return new DoctorOutput(doctor.getDni(), doctor.getAddress(), doctor.getName(), doctor.getCode(),doctor.getWorkingTime(), doctor.getEndWorkingTime(), doctor.getExperience());
    }
}