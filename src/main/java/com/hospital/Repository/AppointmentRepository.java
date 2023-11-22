package com.hospital.Repository;

import com.hospital.Domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByEmployeeCode(String code);

    boolean existsByDniPatientAndDateAppointmentAndTimeAppointment(String dni, LocalDate date, LocalTime time);

    List<Appointment> findByDniPatient(String dni);
    List<Appointment> findByEmployeeCodeAndDateAppointmentAndTimeAppointmentBetween(
            String code, LocalDate date, LocalTime start, LocalTime end);
}