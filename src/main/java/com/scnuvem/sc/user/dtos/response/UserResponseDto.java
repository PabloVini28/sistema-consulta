package com.scnuvem.sc.user.dtos.response;

import com.scnuvem.sc.user.entity.User;
import com.scnuvem.sc.user.enums.Role;

public record UserResponseDto(
    String name,
    String username,
    String email,
    Role role
) {

    public UserResponseDto(User user) {
        this(
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }
    
}
