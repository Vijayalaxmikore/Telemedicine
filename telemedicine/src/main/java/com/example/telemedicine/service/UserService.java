package com.example.telemedicine.service;

import com.example.telemedicine.dto.UserDto;
import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.User;
import com.example.telemedicine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        // Do NOT store plain text passwords in production
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    public User getUserByIdAndRole(Long id, User.Role role) {
        return userRepository.findByIdAndRole(id, role).orElse(null);
    }

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName(); // Or get email, based on your authentication
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Manual mapping
        return convertToDto(user);
    }

    public String getUserRole(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole().toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String getUsernameById(Long userId) {
        return userRepository.findById(userId)
                .map(User::getUsername) // Map the User object to the username
                .orElse(null); // Return null if user is not found
    }

    // Manual mapping from User to UserDto
    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().name());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setDateOfBirth(user.getDateOfBirth());
        userDto.setGender(user.getGender().name());
        userDto.setSpecialization(user.getSpecialization());
        userDto.setLicenseNumber(user.getLicenseNumber());
        userDto.setYearsOfExperience(user.getYearsOfExperience());
        userDto.setAboutMe(user.getAboutMe());
        userDto.setCountry(user.getCountry());
        userDto.setProfilePictureUrl(user.getProfilePictureUrl());
        userDto.setStatus(user.getStatus().name());
        return userDto;
    }



}