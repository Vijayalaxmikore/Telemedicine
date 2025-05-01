package com.example.telemedicine.dto;

import com.example.telemedicine.model.User.Gender;
import com.example.telemedicine.model.User.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String specialization;
    private String licenseNumber;
    private String yearsOfExperience;
    private String aboutMe;
    private String country;
    private String profilePictureUrl;
    private String status;
}