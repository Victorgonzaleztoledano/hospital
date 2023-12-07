package com.hospital.Controller.Output;

import com.hospital.Domain.Nurse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class NurseOutput extends EmployeeOutput {
    public NurseOutput(@NotNull(message = "Dni can not be null") @NotBlank(message = "Dni can not be empty") @Pattern(regexp = "^[0-9]{8}[A-Z]$", message = "Invalid DNI format. Must be 8 numbers followed by a letter") String dni, @NotNull(message = "Address can not be null") @NotBlank(message = "Address can not be empty") String address, @NotNull(message = "Name can not be null") @NotBlank(message = "Name can not be empty") @Pattern(regexp = "^[a-zA-Z]*$", message = "Name can not contains numbers") String name, @Min(value = 0, message = "Code can not be negative") String code, LocalTime workingTime, LocalTime endWorkingTime) {
        super(dni, address, name, code, workingTime, endWorkingTime);
    }

    public static NurseOutput getNurseOutput(Nurse nurse) {
        return new NurseOutput(nurse.getDni(), nurse.getAddress(), nurse.getName(), nurse.getCode(), nurse.getWorkingTime(), nurse.getEndWorkingTime());
    }
}
