package com.hospital.Controller;

import com.hospital.Controller.Input.NurseInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.NurseOutput;
import com.hospital.Exception.*;
import com.hospital.Service.NurseService;
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
@RequestMapping("/nurses")

@RestController
public class NurseController {
    @Autowired
    private NurseService nurseService;

    @GetMapping
    public ResponseEntity<List<NurseOutput>> getNurses() {
        try {
            return ResponseEntity.ok(nurseService.listNurses());
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity addNurse(@Valid @RequestBody NurseInput nurseInput) {
        try {
            nurseService.addNurse(nurseInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DniAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }

    @GetMapping("/{code}/schedule")
    public ResponseEntity<TreeMap<LocalDate, List<LocalTime>>> listAvalibleAppointments(@PathVariable String code) {
        try {
            TreeMap<LocalDate, List<LocalTime>> appointments = nurseService.listAvailibleAppointments(code);
            return ResponseEntity.ok(appointments);
        } catch (NurseNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{code}/schedule/{date}")
    public ResponseEntity<List<LocalTime>> listDateAvalibleAppointments(@PathVariable String code, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<LocalTime> appointments = nurseService.listTimeAvailable(code, date);
            return ResponseEntity.ok(appointments);
        } catch (NurseNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{code}/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAppointments(@PathVariable String code) {
        try {
            return ResponseEntity.ok(nurseService.listAppointmentsByCode(code));
        } catch (EmployeeNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
