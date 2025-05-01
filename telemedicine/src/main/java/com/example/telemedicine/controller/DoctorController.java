package com.example.telemedicine.controller;

import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.User;
import com.example.telemedicine.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Fetch all patients managed by a doctor
    @GetMapping("/patients")
    public ResponseEntity<List<User>> getDoctorPatients(@RequestParam Long doctorId) {
        List<User> patients = doctorService.getPatientsByDoctor(doctorId);
        return ResponseEntity.ok(patients);
    }

    // Fetch all appointments of a doctor
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@RequestParam Long doctorId) {
        List<Appointment> appointments = doctorService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    // Fetch analytics (completed appointments count)
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Long>> getDoctorAnalytics(@RequestParam Long doctorId) {
        Long completedAppointments = doctorService.getCompletedAppointmentsCount(doctorId);
        return ResponseEntity.ok(Map.of("completedAppointments", completedAppointments));
    }

    // POST - Create an appointment
    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment createdAppointment = doctorService.createAppointment(appointment);
        return ResponseEntity.ok(createdAppointment);
    }

    // POST - Update an appointment's status (Pending -> Completed or Cancelled)
    @PostMapping("/appointments/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id, @RequestBody String status) {
        Appointment updatedAppointment = doctorService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(updatedAppointment);
    }

    // POST - Cancel an appointment
    @PostMapping("/appointments/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        doctorService.cancelAppointment(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
