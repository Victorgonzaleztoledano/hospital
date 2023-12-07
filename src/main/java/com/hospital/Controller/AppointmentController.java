package com.hospital.Controller;

import com.hospital.Controller.Input.AppointmentInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Exception.*;
import com.hospital.Service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/appointments")
@Slf4j
@RestController
public class AppointmentController {

    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<AppointmentOutput> listAppointments() throws EmptyListException {
        log.info("Appointments to be listed:");
        return this.appointmentService.listAppointment();
    }

    @PostMapping
    public AppointmentOutput addAppointment(@Valid @RequestBody AppointmentInput appointmentInput) throws NurseNotExistsException, WrongDateException, WrongTimeException, DoctorNotExistsException, PatientNotFoundException, AppointmentAlreadyExistsException, EmployeeNotExistsException {
        AppointmentOutput appointment = appointmentService.addAppointment(appointmentInput);
        log.info("Appointment to be saved: {}", appointment);
        return appointment;
    }
}