package com.example.telemedicine.controller;

import com.example.telemedicine.dto.UserDto;
import com.example.telemedicine.dto.UserLoginDto;
import com.example.telemedicine.model.User;
import com.example.telemedicine.service.AppointmentService;
import com.example.telemedicine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173") // Adjust in production
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(Map.of("message", "Users fetched successfully", "users", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(Map.of("message", "User found", "user", user));
    }

    @PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = userService.getUserByEmail(loginDto.getEmail());
            // Do NOT compare plain text passwords in production
            if (existingUser != null && existingUser.getPassword().equals(loginDto.getPassword())) {
                UserDto userDto = userService.convertToDto(existingUser);
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", userDto);

                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid credentials");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            User existingUser = userService.getUserByEmail(user.getEmail());
            if (existingUser != null) {
                response.put("success", false);
                response.put("message", "Email already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Do NOT store plain text passwords in production
            User savedUser = userService.saveUser(user);

            UserDto userDto = userService.convertToDto(savedUser);

            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("user", userDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<Map<String, Object>> getAllPatients() {
        List<User> patients = userService.getUsersByRole(User.Role.PATIENT);
        return ResponseEntity.ok(Map.of("message", "Patients fetched successfully", "patients", patients));
    }

    @GetMapping("/doctors")
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        List<User> doctors = userService.getUsersByRole(User.Role.DOCTOR);
        System.out.println("getAllDoctors: " + doctors);
        return ResponseEntity.ok(Map.of("message", "Doctors fetched successfully", "doctors", doctors));
    }

    @GetMapping(value = "/{id}/username", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getUsernameById(@PathVariable Long id) {
        String username = userService.getUsernameById(id);

        if (username != null) {
            return ResponseEntity.ok(Map.of("username", username));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/patients/{id}")
    public ResponseEntity<Map<String, Object>> getPatientById(@PathVariable Long id) {
        User patient = userService.getUserByIdAndRole(id, User.Role.PATIENT);
        if (patient == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Patient not found"));
        }
        return ResponseEntity.ok(Map.of("message", "Patient found", "patient", patient));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
        return ResponseEntity.ok(Map.of("message", "User found", "user", user));
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long id) {
        User doctor = userService.getUserByIdAndRole(id, User.Role.DOCTOR);
        if (doctor == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Doctor not found"));
        }
        return ResponseEntity.ok(Map.of("message", "Doctor found", "doctor", doctor));
    }

    @PreAuthorize("hasRole('ADMIN') or #email == authentication.principal.username")
    @GetMapping("/role/{email}")
    public ResponseEntity<Map<String, Object>> getUserRole(@PathVariable String email) {
        try {
            String role = userService.getUserRole(email);
            return ResponseEntity.ok(Map.of("message", "Role fetched successfully", "role", role));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
    }

    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        UserDto userDto = userService.getCurrentUser();
        if (userDto == null) {
            System.out.println("getCurrentUser: User not authenticated.");
            return ResponseEntity.notFound().build();
        }
        System.out.println("getCurrentUser: Returning - " + userDto);
        return ResponseEntity.ok(userDto);
    }


}