package com.scnuvem.sc.user.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record PatchUserUsernameDto(
    @NotBlank(message = "Username cannot be blank")
    String username
) {
    
}
