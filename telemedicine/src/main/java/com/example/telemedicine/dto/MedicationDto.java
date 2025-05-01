package com.example.telemedicine.dto;

import lombok.Data;

@Data
public class MedicationDto {
    private Long id;
    private String name;
    private String dosage;
    private String frequency;
    private String duration;
}