package com.hospital.Controller;

import com.hospital.Controller.Input.AppointmentInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Exception.*;
import com.hospital.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.TreeMap;
@RequestMapping("/appointments")

@RestController
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentOutput>> listAppointments() {
        try {
            return ResponseEntity.ok(appointmentService.listAppointment());
        } catch (EmptyListException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity addAppointment(@Valid @RequestBody AppointmentInput appointmentInput) {
        try {
            appointmentService.addAppointment(appointmentInput);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (PatientNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (EmployeeNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (AppointmentAlreadyExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (WrongDateException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (WrongTimeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (NurseNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (DoctorNotExistsException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/doctors")
    public ResponseEntity<TreeMap<DoctorOutput, List<AppointmentOutput>>> listOcupedDoctors() {
        TreeMap<DoctorOutput, List<AppointmentOutput>> lists = appointmentService.mostScheduledDoctors();
        return ResponseEntity.ok(lists);
    }
}