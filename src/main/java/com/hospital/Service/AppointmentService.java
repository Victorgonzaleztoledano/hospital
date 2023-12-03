package com.hospital.Service;

import com.hospital.Controller.Input.AppointmentInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Domain.Appointment;
import com.hospital.Domain.Doctor;
import com.hospital.Exception.*;
import com.hospital.Repository.AppointmentRepository;
import com.hospital.Repository.DoctorRepository;
import com.hospital.Repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {
    private NurseRepository nurseRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private CommonService commonService;
    private DoctorService doctorService;
    private NurseService nurseService;
    @Autowired
    public AppointmentService(NurseRepository nurseRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, CommonService commonService, DoctorService doctorService, NurseService nurseService) {
        this.nurseRepository = nurseRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.commonService = commonService;
        this.doctorService = doctorService;
        this.nurseService = nurseService;
    }

    //Me crea la cita comprobando que no haya repetido y que todo exista
    public void addAppointment(AppointmentInput a) throws EmployeeNotExistsException, DoctorNotExistsException, NurseNotExistsException, WrongTimeException, WrongDateException, PatientNotFoundException, AppointmentAlreadyExistsException {
        if (!appointmentRepository.existsByDniPatientAndDateAppointmentAndTimeAppointment(a.getDniPatient(), a.getDateAppointment(), a.getTimeAppointment())) {//Comprueba que no exista una cita igual

            if (commonService.comprobateDni(a.getDniPatient())) {  // y que el dni exista

                TreeMap<LocalDate, List<LocalTime>> schedule = getTimeSchedule(a.getCodeEmployee());
                if (schedule.containsKey(a.getDateAppointment())) { //Compruebo que el día es correcto
                    List<LocalTime> timeAvailible = schedule.get(a.getDateAppointment()); //Saco las horas del día

                    boolean created = saveAppointment(timeAvailible, a); //Si dentro del método devuelve false lanzo excepción
                    if (created != true) throw new WrongTimeException("Time of schedule can not be registered");

                } else throw new WrongDateException("Date can not be registered");
            } else throw new PatientNotFoundException("Patient not found");
        } else throw new AppointmentAlreadyExistsException("Patient already has an appointment");
    }

    //Guardar la cita
    private boolean saveAppointment(List<LocalTime> time, AppointmentInput a) {
        for (LocalTime times : time) { //Compruebo que la hora de la cita esté entre las disponibles
            if (times.equals(a.getTimeAppointment())) {
                appointmentRepository.save(Appointment.getAppointment(a));
                return true; // Si se crea da verdadero
            }
        }
        return false;
    }

    //Obtengo el horario del empleado
    private TreeMap<LocalDate, List<LocalTime>> getTimeSchedule(String code) throws DoctorNotExistsException, NurseNotExistsException, EmployeeNotExistsException {
        TreeMap<LocalDate, List<LocalTime>> schedule;
        if (doctorRepository.existsByCode(code)) {
            schedule = doctorService.listAvailibleAppointments(code);
        } else if (nurseRepository.existsByCode(code)) {
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
}