package com.scnuvem.sc.appointment.dtos.request;

import java.time.LocalDate;

import com.scnuvem.sc.appointment.enums.DoctorSpecialty;
import com.scnuvem.sc.appointment.enums.TimeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterAppointmentDto(
    @NotBlank(message = "Patient name cannot be blank")
    String patientName,

    @NotNull
    DoctorSpecialty doctorSpecialty,

    @NotNull(message = "Appointment time cannot be null")
    TimeEnum appointmentTime,

    LocalDate appointmentDate,    

    @NotBlank(message = "Description cannot be blank")
    String description
) {
    
}
