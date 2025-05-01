package com.example.telemedicine.controller;

import com.example.telemedicine.dto.AppointmentDto;
import com.example.telemedicine.dto.UserDto;
import com.example.telemedicine.model.User;
import com.example.telemedicine.service.AdminService;
import com.example.telemedicine.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/users")
    public ResponseEntity<Map<String, List<UserDto>>> getAllUsers() {
        Map<String, List<UserDto>> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to delete user: " + e.getMessage()));
        }
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/appointments/{appointmentId}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody Map<String, String> statusMap) {
        try {
            String status = statusMap.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Status cannot be empty"));
            }

            AppointmentDto updatedAppointment = appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok(Map.of("success", true, "appointment", updatedAppointment));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

}