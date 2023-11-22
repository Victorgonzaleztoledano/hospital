package com.hospital.Controller;

import com.hospital.Controller.Input.DoctorInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Exception.*;
import com.hospital.Service.DoctorService;
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
@RestController
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorOutput>> getDoctors() {
        try {
            return ResponseEntity.ok(doctorService.listDoctors());
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity addDoctor(@Valid @RequestBody DoctorInput doctorInput) {
        try {
            doctorService.addDoctor(doctorInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DniAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{code}/schedule")
    public ResponseEntity<TreeMap<LocalDate, List<LocalTime>>> listTimeAvalible(@PathVariable String code) {
        try {
            TreeMap<LocalDate, List<LocalTime>> appointments = doctorService.listAvailibleAppointments(code);
            return ResponseEntity.ok(appointments);
        } catch (DoctorNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{code}/schedule/{date}")
    public ResponseEntity<List<LocalTime>> listDateAvalibleAppointments(@PathVariable String code, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<LocalTime> appointments = doctorService.listTimeAvailable(code, date);
            return ResponseEntity.ok(appointments);
        } catch (DoctorNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{code}/appointments")
    public ResponseEntity<List<AppointmentOutput>> getAppointments(@PathVariable String code) {
        try {
            return ResponseEntity.ok(doctorService.listAppointmentsByCode(code));
        } catch (EmployeeNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/ordered")
    public ResponseEntity<TreeMap<DoctorOutput, List<AppointmentOutput>>> getBusiestDoctors(){
        try{
            TreeMap<DoctorOutput, List<AppointmentOutput>> busiest = doctorService.getBusiestDoctors();
            return ResponseEntity.ok(busiest);
        }
        catch (EmployeeNotExistsException e){
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
