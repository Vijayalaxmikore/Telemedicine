package com.example.telemedicine.controller;

import com.example.telemedicine.dto.AppointmentDto;
import com.example.telemedicine.dto.PrescriptionDto;
import com.example.telemedicine.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<?> savePrescription(@RequestBody PrescriptionDto prescriptionDto) {
        try {
            PrescriptionDto savedPrescription = prescriptionService.savePrescription(prescriptionDto);
            // Return a success response with the saved prescription data
            return ResponseEntity.ok(Map.of("success", true, "prescription", savedPrescription));
        } catch (RuntimeException e) {
            // Handle errors and return an appropriate error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }



    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getPrescriptionByAppointmentId(@PathVariable Long appointmentId) {
        try {
            PrescriptionDto prescriptionDto = prescriptionService.getPrescriptionByAppointmentId(appointmentId);
            if (prescriptionDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Prescription not found for this appointment ID."));
            }
            return ResponseEntity.ok(prescriptionDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error fetching prescription: " + e.getMessage()));
        }
    }
}