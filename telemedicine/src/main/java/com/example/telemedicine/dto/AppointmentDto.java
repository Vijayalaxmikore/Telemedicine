package com.example.telemedicine.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String appointmentDateTime; // Changed to String
    private String reason;
    private String status = "PENDING"; // Set default status here
}