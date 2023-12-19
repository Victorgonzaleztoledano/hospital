package com.hospital.Controller;

import com.hospital.Controller.Input.NurseInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.NurseOutput;
import com.hospital.Exception.*;
import com.hospital.Service.NurseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

@RequestMapping("/nurses")
@Slf4j
@RestController
@RequiredArgsConstructor
public class NurseController {
    private final NurseService nurseService;

    @GetMapping
    public List<NurseOutput> getNurses() throws EmptyListException {
        log.info("All nurses to be listed:");
        return this.nurseService.listNurses();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NurseOutput addNurse(@Valid @RequestBody NurseInput nurseInput) throws DniAlreadyExistsException {
        NurseOutput nurse = nurseService.addNurse(nurseInput);
        log.info("Nurse to be saved: {}", nurse);
        return nurse;
    }

    @GetMapping("/{code}/schedule")
    public TreeMap<LocalDate, List<LocalTime>> listAvalibleAppointments(@PathVariable String code) throws NurseNotExistsException {
        log.info("Nurse schedule to be listed:");
        return nurseService.listAvailibleAppointments(code);
    }

    @GetMapping("/{code}/schedule/{date}")
    public List<LocalTime> listDateAvalibleAppointments(@PathVariable String code, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NurseNotExistsException {
        log.info("Nurse times of schedule availible:");
        return this.nurseService.listTimeAvailable(code, date);
    }

    @GetMapping("/{code}/appointments")
    public List<AppointmentOutput> getAppointments(@PathVariable String code) throws EmployeeNotExistsException {
        log.info("Nurse appointments to be listed:");
        return this.nurseService.listAppointmentsByCode(code);
    }
}
