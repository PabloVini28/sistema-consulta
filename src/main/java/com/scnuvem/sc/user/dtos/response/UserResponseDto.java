package com.scnuvem.sc.user.dtos.response;

import com.scnuvem.sc.user.enums.Role;

public record UserResponseDto(
    String name,
    String username,
    String email,
    Role role
) {
    
}
