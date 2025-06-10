package com.scnuvem.sc.user.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scnuvem.sc.auth.dtos.request.RegisterUserDto;
import com.scnuvem.sc.user.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) @Size(max=55)
    @Pattern(regexp="^[a-zA-Z\\s]+$", message="Name can only contain letters and spaces")
    private String name;

    @Column(unique=true, nullable=false) @Size(max=25)
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Username can only contain letters, numbers, and underscores")
    private String username;

    @Email
    @Column(unique=true, nullable=false) @Size(max=55)
    @Pattern(regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message="Invalid email format")
    @Size(max=55, message="Email cannot exceed 55 characters") 
    private String email;

    @Column(nullable=false)
    @JsonIgnore
    @Size(min=8, message="Password must be at least 8 characters long")
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", 
             message="Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    @Size(max=255, message="Password cannot exceed 255 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

    @Column(nullable=false)
    @JsonIgnore
    private boolean isEnabled = false;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private LocalDateTime verificationCodeExpiry;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;

    @Column(name="createdAt", nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updatedAt", nullable=false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isEnable(){
        return this.isEnabled;
    }

    public User(RegisterUserDto data, String encryptedPassword, Role role) {
        this.name = data.name();
        this.username = data.username();
        this.email = data.email();
        this.password = encryptedPassword;
        this.role = role;
    }

}
