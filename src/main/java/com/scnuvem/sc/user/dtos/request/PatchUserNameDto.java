package com.scnuvem.sc.user.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record PatchUserNameDto(
    @NotBlank(message = "Name cannot be blank")
    String name
) {
}
