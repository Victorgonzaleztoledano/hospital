package com.hospital.Service;

import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Domain.Appointment;
import com.hospital.Exception.EmployeeNotExistsException;
import com.hospital.Repository.AppointmentRepository;
import com.hospital.Repository.DoctorRepository;
import com.hospital.Repository.NurseRepository;
import com.hospital.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

@Service
public class CommonService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private NurseRepository nurseRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public boolean comprobateDni(String dni) {
        if (patientRepository.existsById(dni)) return true;
        else if (doctorRepository.existsById(dni)) return true;
        else if (nurseRepository.existsById(dni)) return true;
        else return false;
    }

    public int compare(AppointmentOutput appointment1, AppointmentOutput appointment2) {
        int dateComparison = appointment1.getDateAppointment().compareTo(appointment2.getDateAppointment());
        if (dateComparison == 0) {
            return appointment1.getTimeAppointment().compareTo(appointment2.getTimeAppointment());
        }
        return dateComparison;
    }

    public List<AppointmentOutput> listAppointmentsByCode(String code) throws EmployeeNotExistsException {
        if (doctorRepository.existsByCode(code) || nurseRepository.existsByCode(code)) {
            List<Appointment> appointments = appointmentRepository.findByEmployeeCode(code);
            //Ordena por fecha y hora
            List<AppointmentOutput> sortedAppointments = new ArrayList<>();
            for (Appointment appointment : appointments) {
                sortedAppointments.add(AppointmentOutput.getAppointmentOutput(appointment));
            }
            Collections.sort(sortedAppointments, this::compare);
            return sortedAppointments;
        } else throw new EmployeeNotExistsException("Doctor not found");
    }

    public List<LocalTime> listTimeAvailableCommon(String code, LocalDate date, LocalTime start, LocalTime end) {
        List<LocalTime> appointments = new ArrayList<>();
        //Rellena el array comprobando no guardar las ya registradas
        while (start.isBefore(end)) {
            if (appointmentRepository.existsByDateAppointmentAndTimeAppointmentAndEmployeeCode(date, start, code)) {
                start = start.plusHours(1);
                continue;
                //Si encuentra una repetida salta a la siguiente vuelta del bucle
            }
            appointments.add(start);
            start = start.plusHours(1);
        }
        return appointments;
    }
    public TreeMap<LocalDate, List<LocalTime>> listAvailibleAppointmentsCommon(String code, LocalTime start, LocalTime end) {
        //Me saca la fecha de hoy, calcula hasta el lunes más próximo y saca el viernes sumándole 5 días
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonday = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextFriday = nextMonday.plusDays(5);

        //Rellena el treemap con los días como clave y el horario como valor
        TreeMap<LocalDate, List<LocalTime>> availibleAppointments = new TreeMap<>();
        for (LocalDate i = nextMonday; i.isBefore(nextFriday); i = i.plusDays(1)) {
            List<LocalTime> appointmentsForDay = new ArrayList<>(listTimeAvailableCommon(code, i, start, end));
            availibleAppointments.put(i, appointmentsForDay);
        }
        return availibleAppointments;
    }
}