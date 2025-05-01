package com.example.telemedicine.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionDto {
    private Long id;
    private Long appointmentId;
    private String diagnosis;
    private List<MedicationDto> medications;
    private String notes;
    private LocalDateTime date;
}