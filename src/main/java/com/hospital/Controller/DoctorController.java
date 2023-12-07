package com.hospital.Controller;

import com.hospital.Controller.Input.DoctorInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Exception.*;
import com.hospital.Service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

@RequestMapping("/doctors")
@Slf4j
@RestController
public class DoctorController {
    private DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<DoctorOutput> getDoctors() throws EmptyListException {
        log.info("Doctors to be listed:");
        return this.doctorService.listDoctors();
    }

    @PostMapping
    public DoctorOutput addDoctor(@Valid @RequestBody DoctorInput doctorInput) throws DniAlreadyExistsException {
        DoctorOutput doctor = doctorService.addDoctor(doctorInput);
        log.info("Doctor to be saved: {}", doctor);
        return doctor;
    }

    @GetMapping("/{code}/schedule")
    public TreeMap<LocalDate, List<LocalTime>> listTimeAvalible(@PathVariable String code) throws DoctorNotExistsException {
        log.info("Doctor schedule to be listed:");
        return this.doctorService.listAvailibleAppointments(code);
    }

    @GetMapping("/{code}/schedule/{date}")
    public List<LocalTime> listDateAvalibleAppointments(@PathVariable String code, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws DoctorNotExistsException {
        log.info("Doctor times of schedule availible:");
        return this.doctorService.listTimeAvailable(code, date);
    }

    @GetMapping("/{code}/appointments")
    public List<AppointmentOutput> getAppointments(@PathVariable String code) throws EmployeeNotExistsException {
        log.info("Doctor appointments to be listed:");
        return this.doctorService.listAppointmentsByCode(code);
    }

    @GetMapping("/ordered")
    public TreeMap<String, List<AppointmentOutput>> getBusiestDoctors() {
        log.info("Doctors ordered to be listed:");
        return this.doctorService.ordered();
    }
}
