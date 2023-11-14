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
    @Autowired
    private NurseRepository nurseRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private NurseService nurseService;

    //Me crea la cita comprobando que no haya repetido y que todo exista
    public void addAppointment(AppointmentInput a) throws EmployeeNotExistsException, DoctorNotExistsException, NurseNotExistsException, WrongTimeException, WrongDateException, PatientNotFoundException, AppointmentAlreadyExistsException  {
        if (!appointmentRepository.existsByDniPatientAndDateAppointmentAndTimeAppointment(a.getDniPatient(), a.getDateAppointment(), a.getTimeAppointment())) {//Comprueba que no exista una cita igual
            if (commonService.comprobateDni(a.getDniPatient())) {  // y que el dni exista
                TreeMap<LocalDate, List<LocalTime>> schedule;
                if(doctorRepository.existsByCode(a.getCodeEmployee())){
                    schedule = doctorService.listAvailibleAppointments(a.getCodeEmployee());
                }
                else if(nurseRepository.existsByCode(a.getCodeEmployee())){
                    schedule = nurseService.listAvailibleAppointments(a.getCodeEmployee());
                }
                else throw new EmployeeNotExistsException("Employee code not found");
                if (schedule.containsKey(a.getDateAppointment())) { //Compruebo que el día es correcto
                    List<LocalTime> timeAvailible = schedule.get(a.getDateAppointment()); //Saco las horas del día
                    boolean created = false; //Si no se crea la cita al salir del bucle hago excepción
                    for (LocalTime time : timeAvailible) { //Compruebo que la hora de la cita esté entre las disponibles
                        if (time.equals(a.getTimeAppointment())) {
                            appointmentRepository.save(Appointment.getAppointment(a));
                            created = true; // Si se crea da verdadero
                        }
                    } //Si no es verdadero que se crea salta la última excepción
                    if (created == false) throw new WrongTimeException("Time of schedule can not be registered");
                } else throw new WrongDateException("Date can not be registered");
            } else throw new PatientNotFoundException("Patient not found");
        } else throw new AppointmentAlreadyExistsException("Patient already has an appointment");
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

    ////////////FALTA QUE LO ORDENE POR MÁS OCUPADO//////////
    public TreeMap<DoctorOutput, List<AppointmentOutput>> mostScheduledDoctors() {
        TreeMap<DoctorOutput, List<AppointmentOutput>> employeeAppointments = new TreeMap<>((o1, o2) -> {
            //Ordena de más a menos las listas del treemap
            int size1 = 0;
            int size2 = 0;
            if (o1 instanceof List) size1 = ((List<?>) o1).size();
            if (o2 instanceof List) size2 = ((List<?>) o2).size();
            return Integer.compare(size1, size2);
        });
        List<Doctor> doctors = doctorRepository.findAll();
        for (Doctor doctor : doctors) {
            List<AppointmentOutput> outputs = new ArrayList<>();
            List<Appointment> doctorAppointments = appointmentRepository.findByEmployeeCode(doctor.getCode());
            for (Appointment appointment : doctorAppointments) {
                outputs.add(AppointmentOutput.getAppointmentOutput(appointment));
            }
            employeeAppointments.put(DoctorOutput.getDoctorOutput(doctor), outputs);
        }
        return employeeAppointments;
    }
}