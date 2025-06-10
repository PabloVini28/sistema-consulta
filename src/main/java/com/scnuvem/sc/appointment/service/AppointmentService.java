package com.scnuvem.sc.appointment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scnuvem.sc.appointment.dtos.request.RegisterAppointmentDto;
import com.scnuvem.sc.appointment.dtos.response.AppointmentResponseDto;
import com.scnuvem.sc.appointment.entity.Appointment;
import com.scnuvem.sc.appointment.repository.AppointmentRepository;
import com.scnuvem.sc.user.entity.User;
import com.scnuvem.sc.user.repository.UserRepository;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    public AppointmentResponseDto createAppointment(RegisterAppointmentDto data) {
        Optional<User> patient = userRepository.findByName(data.patientName());

        if (patient.isEmpty()) {
            throw new IllegalArgumentException("Patient not found");
        }

        LocalDate date = data.appointmentDate();

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Appointment date cannot be in the past");
        }

        if (date.isAfter(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("Appointment date cannot be more than one year in the future");
        }

        if (data.appointmentTime() == null) {
            throw new IllegalArgumentException("Appointment time cannot be null");
        }
        
        if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
            throw new IllegalArgumentException("Appointment cannot be on a Saturday or Sunday");
        }
        // Check if an appointment already exists for the given date, time, and doctor's specialty
        Optional<Appointment> appointment = appointmentRepository.findByDateAndTimeAndDoctorSpecialty(
            date, data.appointmentTime(), data.doctorSpecialty()
        );

        if (appointment.isPresent()) {
            throw new IllegalArgumentException("Appointment already exists for this date and time");
        }
        
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient.get());
        newAppointment.setDoctorSpecialty(data.doctorSpecialty());
        newAppointment.setAppointmentDate(date);
        newAppointment.setAppointmentTime(data.appointmentTime());
        newAppointment.setDescription(data.description());

        Appointment savedAppointment = appointmentRepository.save(newAppointment);
        return new AppointmentResponseDto(savedAppointment);
    }

    public AppointmentResponseDto getAppointmentById(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        return new AppointmentResponseDto(appointment.get());
    }

    public void deleteAppointment(Long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found");
        }
        appointmentRepository.delete(appointment.get());
    }

    public List<AppointmentResponseDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
            .map(AppointmentResponseDto::new)
            .collect(Collectors.toList());
    }

}
