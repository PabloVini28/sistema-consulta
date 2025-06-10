package com.scnuvem.sc.appointment.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.scnuvem.sc.appointment.enums.DoctorSpecialty;
import com.scnuvem.sc.appointment.enums.TimeEnum;
import com.scnuvem.sc.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    @Enumerated
    private DoctorSpecialty doctorSpecialty;

    @Column(name="appointmentDate", nullable=false)
    private LocalDate appointmentDate;

    @Column(name="appointmentDate", nullable=false)
    private TimeEnum appointmentTime;

    @Column(nullable=false) @Size(max=255)
    private String description;

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

}
