package com.scnuvem.sc.appointment.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatchAppointmentDescription(
    @NotNull
    Long appointmentId,
    
    @NotBlank
    String description
) {
    
}
