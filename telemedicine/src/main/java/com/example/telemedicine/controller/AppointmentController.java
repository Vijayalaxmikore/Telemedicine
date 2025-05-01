package com.example.telemedicine.controller;

import com.example.telemedicine.dto.AppointmentDto;
import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.User;
import com.example.telemedicine.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/book")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody AppointmentDto appointmentDto) {
        try {
            AppointmentDto savedAppointment = appointmentService.bookAppointment(appointmentDto);
            return ResponseEntity.ok(Map.of("success", true, "message", "Appointment booked successfully", "appointment", savedAppointment));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        try {
            AppointmentDto appointment = appointmentService.getAppointmentById(id);
            if (appointment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAppointmentsForPatient(@RequestParam("patientId") Long patientId) {
        try {
            List<AppointmentDto> appointments = appointmentService.getAppointmentsForPatient(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        try {
            List<AppointmentDto> appointments = appointmentService.getAppointmentsByDoctorId(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        try {
            List<AppointmentDto> appointments = appointmentService.getAppointmentsByDoctorId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // ... other controller methods
}