package com.hospital.Service;

import com.hospital.Controller.Input.PatientInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.PatientOutput;
import com.hospital.Domain.Appointment;
import com.hospital.Domain.Patient;
import com.hospital.Exception.*;
import com.hospital.Repository.AppointmentRepository;
import com.hospital.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PatientService {
    private CommonService commonService;
    private PatientRepository patientRepository;
    private AppointmentRepository appointmentRepository;
    @Autowired
    public PatientService(CommonService commonService, PatientRepository patientRepository, AppointmentRepository appointmentRepository) {
        this.commonService = commonService;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    //Agregar paciente
    public PatientOutput addPatient(PatientInput patientInput) throws DniAlreadyExistsException {
        if(!commonService.comprobateDni(patientInput.getDni())) {
            Patient patient = Patient.getPatient(patientInput);
            patientRepository.save(patient);
            return PatientOutput.getPatientOutput(patient);
        }
        else throw new DniAlreadyExistsException("Dni is already registered");
    }

    //Listar todos los pacientes
    public List<PatientOutput> listPatients() throws EmptyListException {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) throw new EmptyListException("No patients registered");
        List<PatientOutput> patientOutputs = new ArrayList<>();
        for (Patient patient : patients) {
            patientOutputs.add(PatientOutput.getPatientOutput(patient));
        }
        return patientOutputs;
    }

    //Lista las citas de un paciente
    public List<AppointmentOutput> listPatientAppointments(String dni) throws PatientNotFoundException {
        if (!patientRepository.existsById(dni)) throw new PatientNotFoundException("Dni does not exists");
        List<Appointment> appointments = appointmentRepository.findByDniPatient(dni);
        //Ordena por fecha y hora
        List<AppointmentOutput> sortedAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            sortedAppointments.add(AppointmentOutput.getAppointmentOutput(appointment));
        }
        Collections.sort(sortedAppointments, commonService::compare);
        return sortedAppointments;
    }
}
