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
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private NurseRepository nurseRepository;
    private AppointmentRepository appointmentRepository;
    @Autowired
    public CommonService(PatientRepository patientRepository, DoctorRepository doctorRepository, NurseRepository nurseRepository, AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.appointmentRepository = appointmentRepository;
    }

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
        List<Appointment> appointmentsRepo = appointmentRepository.findByEmployeeCodeAndDateAppointmentAndTimeAppointmentBetween(code, date, start, end);
        while (start.isBefore(end)) {
            LocalTime finalStart = start;
            if (appointmentsRepo.stream().anyMatch(appointment -> appointment.getTimeAppointment().equals(finalStart))) {
                start = start.plusHours(1);
                continue;
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