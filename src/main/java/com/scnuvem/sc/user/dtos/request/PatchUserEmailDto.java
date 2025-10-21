package com.scnuvem.sc.user.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record PatchUserEmailDto(
    @NotBlank
    String email
) {
    
}
