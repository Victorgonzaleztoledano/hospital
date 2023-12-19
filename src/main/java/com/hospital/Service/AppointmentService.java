package com.hospital.Service;

import com.hospital.Controller.Input.AppointmentInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Domain.Appointment;
import com.hospital.Exception.*;
import com.hospital.Repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CommonService commonService;
    private final DoctorService doctorService;
    private final NurseService nurseService;

    //Me crea la cita comprobando que no haya repetido y que todo exista
    public AppointmentOutput addAppointment(AppointmentInput a) throws EmployeeNotExistsException, DoctorNotExistsException, NurseNotExistsException, WrongTimeException, WrongDateException, PatientNotFoundException, AppointmentAlreadyExistsException {
        //Comprueba que no exista una cita igual
        boolean appointmentAlreadyExists = appointmentRepository.existsByDniPatientAndDateAppointmentAndTimeAppointment(a.getDniPatient(), a.getDateAppointment(), a.getTimeAppointment());

        //y que el dni exista
        boolean patientDniExists = commonService.comprobateDni(a.getDniPatient());

        if (appointmentAlreadyExists) throw new AppointmentAlreadyExistsException("Patient already has an appointment");
        if (!patientDniExists) throw new PatientNotFoundException("Patient not found");

        //Saco el horario de cada día del empleado
        TreeMap<LocalDate, List<LocalTime>> schedule = getTimeSchedule(a.getCodeEmployee());

        //Compruebo que el día está disponible
        if (!schedule.containsKey(a.getDateAppointment())) throw new WrongDateException("Date can not be registered");

        //Saco las horas del día en concreto
        List<LocalTime> timeAvailible = schedule.get(a.getDateAppointment());

        //Compruebo si coincide la hora de la cita con las disponibles
        Appointment appointmentSave = saveAppointment(timeAvailible, a);
        if (appointmentSave == null) throw new WrongTimeException("Time of schedule can not be registered");

        return AppointmentOutput.getAppointmentOutput(appointmentSave);
    }

    //Guardar la cita
    private Appointment saveAppointment(List<LocalTime> time, AppointmentInput a) {
        //Compruebo que la hora de la cita esté entre las disponibles
        for (LocalTime times : time) {
            if (times.equals(a.getTimeAppointment())) {
                Appointment appointment = Appointment.getAppointment(a);
                appointmentRepository.save(appointment);
                return appointment;
            }
        }
        return null;
    }

    //Obtengo el horario del empleado
    private TreeMap<LocalDate, List<LocalTime>> getTimeSchedule(String code) throws DoctorNotExistsException, NurseNotExistsException, EmployeeNotExistsException {
        TreeMap<LocalDate, List<LocalTime>> schedule;
        if (doctorService.existsByCode(code)) {
            schedule = doctorService.listAvailibleAppointments(code);
        } else if (nurseService.existsByCode(code)) {
            schedule = nurseService.listAvailibleAppointments(code);
        } else throw new EmployeeNotExistsException("Employee code not found");
        return schedule;
    }

    //Imprime todas las citas
    public List<AppointmentOutput> listAppointment() throws EmptyListException {
        List<Appointment> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) throw new EmptyListException("No appointments registered");
        List<AppointmentOutput> appointmentOutputs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointmentOutputs.add(AppointmentOutput.getAppointmentOutput(appointment));
        }
        return appointmentOutputs;
    }

    public List<Appointment> findByEmployeeCode(String code){
        return appointmentRepository.findByEmployeeCode(code);
    }

    public List<Appointment> findByDniPatient(String dni) {
        return appointmentRepository.findByDniPatient(dni);
    }
}