package com.hospital.Service;

import com.hospital.Controller.Input.NurseInput;
import com.hospital.Controller.Output.AppointmentOutput;
import com.hospital.Controller.Output.NurseOutput;
import com.hospital.Domain.Nurse;
import com.hospital.Exception.*;
import com.hospital.Repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class NurseService {

    private final NurseRepository nurseRepository;
    private final CommonService commonService;

    //Agrega un enfermero
    public NurseOutput addNurse(NurseInput nurseInput) throws DniAlreadyExistsException {
        if (!commonService.comprobateDni(nurseInput.getDni())) {
            Nurse nurse = Nurse.getNurse(nurseInput);
            nurseRepository.save(nurse);
            return NurseOutput.getNurseOutput(nurse);
        } else throw new DniAlreadyExistsException("Dni is already registered");
    }

    //Imprime todos los enfermeros
    public List<NurseOutput> listNurses() throws EmptyListException {
        List<Nurse> nurses = nurseRepository.findAll();
        if (nurses.isEmpty()) throw new EmptyListException("No nurses registered");
        List<NurseOutput> nurseOutputs = new ArrayList<>();
        for (Nurse nurse : nurses) {
            nurseOutputs.add(NurseOutput.getNurseOutput(nurse));
        }
        return nurseOutputs;
    }

    //Obtiene el horario para un día en concreto
    public List<LocalTime> listTimeAvailable(String code, LocalDate date) throws NurseNotExistsException {
        Nurse nurse = nurseRepository.findByCode(code);
        if (nurse == null) throw new NurseNotExistsException("Nurse not found");
        return commonService.listTimeAvailableCommon(code, date, nurse.getWorkingTime(), nurse.getEndWorkingTime());
    }

    //Devuelve un map con un horario por cada día
    public TreeMap<LocalDate, List<LocalTime>> listAvailibleAppointments(String code) throws NurseNotExistsException {
        Nurse nurse = nurseRepository.findByCode(code);
        if (nurse == null) throw new NurseNotExistsException("Nurse not found");
        return commonService.listAvailibleAppointmentsCommon(code, nurse.getWorkingTime(), nurse.getEndWorkingTime());
    }

    //Imprime las citas de un empleado a través de su codigo de empleado
    public List<AppointmentOutput> listAppointmentsByCode(String code) throws EmployeeNotExistsException {
        return commonService.listAppointmentsByCode(code);
    }

    public boolean existsByCode(String code){
        return nurseRepository.existsByCode(code);
    }
}