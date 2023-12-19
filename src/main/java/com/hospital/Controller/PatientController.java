package com.hospital.Controller;

import com.hospital.Controller.Input.PatientInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.PatientOutput;
import com.hospital.Exception.*;
import com.hospital.Service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/patients")
@Slf4j
@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientOutput> getPatients() throws EmptyListException {
        log.info("All patients to be listed:");
        return this.patientService.listPatients();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientOutput addPatient(@Valid @RequestBody PatientInput patientInput) throws DniAlreadyExistsException {
        PatientOutput patient = patientService.addPatient(patientInput);
        log.info("Patient to be saved: {}", patient);
        return patient;
    }

    @GetMapping("/{dni}/appointments")
    public List<AppointmentOutput> getAppointmentsPatients(@PathVariable String dni) throws PatientNotFoundException {
        log.info("Appointments from " + dni +" to be listed:");
        return this.patientService.listPatientAppointments(dni);
    }
}