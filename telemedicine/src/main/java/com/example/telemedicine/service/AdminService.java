package com.example.telemedicine.service;

import com.example.telemedicine.dto.UserDto;
import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.User;
import com.example.telemedicine.repository.AppointmentRepository;
import com.example.telemedicine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Map<String, List<UserDto>> getAllUsers() {
        List<User> doctors = userRepository.findByRole(User.Role.DOCTOR);
        List<User> patients = userRepository.findByRole(User.Role.PATIENT);

        List<UserDto> doctorDtos = doctors.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());

        List<UserDto> patientDtos = patients.stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());

        Map<String, List<UserDto>> usersMap = new HashMap<>();
        usersMap.put("doctors", doctorDtos);
        usersMap.put("patients", patientDtos);

        return usersMap;
    }

    public Map<String, Object> getStats() {
        // Example implementation (adjust according to your needs)
        long totalUsers = userRepository.count();
        long activeDoctors = userRepository.countByRoleAndStatus(User.Role.DOCTOR, User.Status.ACTIVE);
        long totalAppointments = appointmentRepository.count();

        // Calculate monthly and annual revenue (example calculation)
        LocalDateTime startOfCurrentMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        LocalDateTime endOfCurrentMonth = LocalDateTime.of(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()), LocalTime.MAX);
        List<Appointment> monthlyAppointments = appointmentRepository.findByAppointmentDateTimeBetween(startOfCurrentMonth, endOfCurrentMonth);

        double monthlyRevenue = calculateRevenue(monthlyAppointments);

        LocalDateTime startOfCurrentYear = LocalDateTime.of(LocalDate.now().withDayOfYear(1), LocalTime.MIN);
        LocalDateTime endOfCurrentYear = LocalDateTime.of(LocalDate.now().withDayOfYear(LocalDate.now().isLeapYear() ? 366 : 365), LocalTime.MAX);
        List<Appointment> yearlyAppointments = appointmentRepository.findByAppointmentDateTimeBetween(startOfCurrentYear, endOfCurrentYear);

        double annualRevenue = calculateRevenue(yearlyAppointments);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("activeDoctors", activeDoctors);
        stats.put("totalAppointments", totalAppointments);
        stats.put("monthlyRevenue", monthlyRevenue);
        stats.put("annualRevenue", annualRevenue);

        return stats;
    }



    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // Helper method to calculate revenue (replace with your actual logic)
    private double calculateRevenue(List<Appointment> appointments) {
        // Example: Assume each appointment costs $50
        return appointments.size() * 50.0;
    }

    // In AdminService.java
    private UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());

        // **Make sure you are setting all the other fields here:**
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setGender(user.getGender() != null ? user.getGender().name() : null); // Handle null
        userDto.setSpecialization(user.getSpecialization());
        userDto.setLicenseNumber(user.getLicenseNumber());
        userDto.setYearsOfExperience(user.getYearsOfExperience());
        userDto.setAboutMe(user.getAboutMe());
        userDto.setCountry(user.getCountry());
        userDto.setProfilePictureUrl(user.getProfilePictureUrl());
        userDto.setStatus(user.getStatus() != null ? user.getStatus().name() : null); // Handle null

        return userDto;
    }

}