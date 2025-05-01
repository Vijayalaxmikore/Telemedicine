package com.example.telemedicine.service;

import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.User;
import com.example.telemedicine.repository.AppointmentRepository;
import com.example.telemedicine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DoctorService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    // Fetch all patients managed by a doctor
    public List<User> getPatientsByDoctor(Long doctorId) {
        // This should be a custom query based on how you structure your appointments and relationships
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(Appointment::getPatient)  // Assuming Appointment has a 'getPatient()' method
                .toList();
    }

    // Fetch all appointments of a doctor
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    // Fetch analytics (completed appointments count)
    public Long getCompletedAppointmentsCount(Long doctorId) {
        return appointmentRepository.countByDoctorIdAndStatus(doctorId, "COMPLETED");
    }

    // Create a new appointment
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Update appointment status
    public Appointment updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(status));
        return appointmentRepository.save(appointment);
    }

    // Cancel an appointment
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(Appointment.AppointmentStatus.valueOf("CANCELLED"));
        appointmentRepository.save(appointment);
    }
}
