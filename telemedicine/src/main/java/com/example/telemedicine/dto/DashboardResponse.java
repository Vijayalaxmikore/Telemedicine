package com.example.telemedicine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DashboardResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommonData {
        private String announcements;
        private String notifications;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientData {
        private List<String> appointments; // List of upcoming appointment details
        private String medicalHistory;     // Brief summary of medical history
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorData {
        private List<String> patients; // List of patient names or IDs
        private String schedule;       // Doctor's schedule or availability
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminData {
        private String userStats;  // Statistics about users
        private String reports;    // Platform reports or analytics
    }
}
