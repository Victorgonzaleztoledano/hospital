package com.hospital.Service;

import com.hospital.Controller.Input.DoctorInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Domain.Doctor;

import com.hospital.Exception.*;
import com.hospital.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class DoctorService {
    @Autowired
    private CommonService commonService;
    @Autowired
    private DoctorRepository doctorRepository;

    //Agrego un doctor
    public void addDoctor(DoctorInput doctorInput) throws DniAlreadyExistsException {
        if(!commonService.comprobateDni(doctorInput.getDni())) {
            Doctor doctor = Doctor.getDoctor(doctorInput);
            doctorRepository.save(doctor);
        }
        else throw new DniAlreadyExistsException("Dni is already registered");
    }
    //Listo todos los doctores del repositorio
    public List<DoctorOutput> listDoctors() throws EmptyListException {
        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) throw new EmptyListException("No doctors registered");
        List<DoctorOutput> doctorOutputs = new ArrayList<>();
        for (Doctor doctor : doctors) {
            doctorOutputs.add(DoctorOutput.getDoctorOutput(doctor));
        }
        return doctorOutputs;
    }
    //Listado de horas disponibles de un médico y día
    public List<LocalTime> listTimeAvailable(String code, LocalDate date) throws DoctorNotExistsException {
        Doctor doctor = doctorRepository.findByCode(code);
        if (doctor == null) throw new DoctorNotExistsException("Doctor not found");
        return commonService.listTimeAvailableCommon(code, date, doctor.getWorkingTime(), doctor.getEndWorkingTime());
    }

    //Devuelve un map con un horario por cada día
    public TreeMap<LocalDate, List<LocalTime>> listAvailibleAppointments(String code) throws DoctorNotExistsException {
        Doctor doctor = doctorRepository.findByCode(code);
        if (doctor == null) throw new DoctorNotExistsException("Doctor not found");
        return commonService.listAvailibleAppointmentsCommon(code, doctor.getWorkingTime(), doctor.getEndWorkingTime());
    }

    //Imprime las citas de un empleado a través de su codigo de empleado
    public List<AppointmentOutput> listAppointmentsByCode(String code) throws EmployeeNotExistsException  {
        return commonService.listAppointmentsByCode(code);
    }
}