package com.scnuvem.sc.appointment.dtos.response;

import java.time.LocalDate;

import com.scnuvem.sc.appointment.entity.Appointment;
import com.scnuvem.sc.appointment.enums.TimeEnum;

public record AppointmentResponseDto(
    Long id,
    String patientName,
    String doctorSpecialty,
    LocalDate appointmentDate,
    TimeEnum appointmentTime,
    String description
) {

    public AppointmentResponseDto(Appointment appointment) {
        this(
            appointment.getId(),
            appointment.getPatient().getName(),
            appointment.getDoctorSpecialty().name(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getDescription()
        );
    }
    
}
