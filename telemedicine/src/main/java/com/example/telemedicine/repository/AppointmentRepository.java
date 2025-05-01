package com.example.telemedicine.repository;

import com.example.telemedicine.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    Long countByDoctorIdAndStatus(Long doctorId, String status);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime = :appointmentDateTime")
    List<Appointment> findByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime dateTime);

    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
