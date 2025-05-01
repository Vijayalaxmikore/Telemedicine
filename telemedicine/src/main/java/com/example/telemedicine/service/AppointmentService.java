package com.example.telemedicine.service;

import com.example.telemedicine.dto.AppointmentDto;
import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.User;
import com.example.telemedicine.repository.AppointmentRepository;
import com.example.telemedicine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;



    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(patientId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public boolean isDoctorAvailable(Long doctorId, String dateTimeString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        // Check if there's an appointment for the doctor at the given time
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentDateTime(doctorId, dateTime);

        // If the list is empty, the doctor is available
        return appointments.isEmpty();
    }


    public AppointmentDto bookAppointment(AppointmentDto appointmentDto) {
        User patient = userRepository.findById(appointmentDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User doctor = userRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Parse date and time string to LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime appointmentDateTime = LocalDateTime.parse(appointmentDto.getAppointmentDateTime(), formatter);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(appointmentDateTime)
                .reason(appointmentDto.getReason())
                .status(Appointment.AppointmentStatus.PENDING)
                .build();

        appointment = appointmentRepository.save(appointment);

        return convertToDto(appointment);
    }

    public List<AppointmentDto> getAppointmentsForPatient(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        // Format LocalDateTime to String
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        dto.setReason(appointment.getReason());
        dto.setStatus(appointment.getStatus().name());
        return dto;
    }

    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return convertToDto(appointment);
    }

    public List<AppointmentDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        try {
            Appointment.AppointmentStatus appointmentStatus = Appointment.AppointmentStatus.valueOf(status.toUpperCase());
            appointment.setStatus(appointmentStatus);
            appointment = appointmentRepository.save(appointment);
            return convertToDto(appointment);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status provided: " + status);
        }
    }

}