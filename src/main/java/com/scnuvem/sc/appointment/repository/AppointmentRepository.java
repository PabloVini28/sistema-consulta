package com.scnuvem.sc.appointment.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scnuvem.sc.appointment.entity.Appointment;
import com.scnuvem.sc.appointment.enums.DoctorSpecialty;
import com.scnuvem.sc.appointment.enums.TimeEnum;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    Optional<Appointment> findByDateAndTimeAndDoctorSpecialty(LocalDate Date, TimeEnum time, DoctorSpecialty doctorSpecialty);
}
