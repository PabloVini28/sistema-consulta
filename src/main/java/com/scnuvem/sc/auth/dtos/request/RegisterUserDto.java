package com.scnuvem.sc.auth.dtos.request;

public record RegisterUserDto(
    String name,
    String username,
    String email,
    String password
) {
    
}
