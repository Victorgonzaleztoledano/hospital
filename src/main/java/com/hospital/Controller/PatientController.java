package com.hospital.Controller;

import com.hospital.Controller.Input.PatientInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.PatientOutput;
import com.hospital.Exception.*;
import com.hospital.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RequestMapping("/patients")

@RestController
public class PatientController {
    private PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientOutput>> getPatients() {
        try {
            return ResponseEntity.ok(patientService.listPatients());
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity addPatient(@Valid @RequestBody PatientInput patientInput) {
        try {
            patientService.addPatient(patientInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DniAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("/{dni}/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAppointmentsPatients(@PathVariable String dni) {
        try {
            return ResponseEntity.ok(patientService.listPatientAppointments(dni));
        } catch (PatientNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}