package com.hospital.Service;

import com.hospital.Controller.Input.DoctorInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.DoctorCodeOutPut;
import com.hospital.Controller.Output.DoctorOutput;
import com.hospital.Domain.Appointment;
import com.hospital.Domain.Doctor;

import com.hospital.Exception.*;
import com.hospital.Repository.AppointmentRepository;
import com.hospital.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorService {
    private CommonService commonService;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    @Autowired
    public DoctorService(CommonService commonService, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.commonService = commonService;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    //Agrego un doctor
    public DoctorOutput addDoctor(DoctorInput doctorInput) throws DniAlreadyExistsException {
        if(!commonService.comprobateDni(doctorInput.getDni())) {
            Doctor doctor = Doctor.getDoctor(doctorInput);
            doctorRepository.save(doctor);
            return DoctorOutput.getDoctorOutput(doctor);
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
    //Ordena los doctores por más ocupados
    public TreeMap<String, List<AppointmentOutput>> ordered() {
        List<Doctor> doctors = doctorRepository.findAll();
        TreeMap<String, List<AppointmentOutput>> ordered = new TreeMap<>(Collections.reverseOrder());

        for (Doctor doctor : doctors) {
            DoctorCodeOutPut doctorOutput = new DoctorCodeOutPut(doctor.getCode());

            List<Appointment> appointments = appointmentRepository.findByEmployeeCode(doctor.getCode());
            List<AppointmentOutput> outPut = new ArrayList<>();

            for (Appointment appointment : appointments) {
                AppointmentOutput appointmentOutput = AppointmentOutput.getAppointmentOutput(appointment);
                outPut.add(appointmentOutput);
            }

            outPut.sort(Comparator.comparing(AppointmentOutput::getTimeAppointment));

            ordered.put(doctorOutput.getCode(), outPut);
        }
        return ordered;
    }
}