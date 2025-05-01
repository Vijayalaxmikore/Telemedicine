package com.example.telemedicine.service;

import com.example.telemedicine.dto.MedicationDto;
import com.example.telemedicine.dto.PrescriptionDto;
import com.example.telemedicine.model.Appointment;
import com.example.telemedicine.model.Medication;
import com.example.telemedicine.model.Prescription;
import com.example.telemedicine.repository.AppointmentRepository;
import com.example.telemedicine.repository.MedicationRepository;
import com.example.telemedicine.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public PrescriptionDto savePrescription(PrescriptionDto prescriptionDto) {
        Prescription prescription = new Prescription();

        // Fetch the associated appointment
        Appointment appointment = appointmentRepository.findById(prescriptionDto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        prescription.setAppointment(appointment);

        prescription.setDiagnosis(prescriptionDto.getDiagnosis());
        prescription.setNotes(prescriptionDto.getNotes());
        prescription.setDate(LocalDateTime.now()); // Set current date and time

        // Manually convert MedicationDTOs to Medication entities
        List<Medication> medications = prescriptionDto.getMedications().stream()
                .map(medicationDto -> convertToMedicationEntity(medicationDto, prescription))
                .collect(Collectors.toList());

        prescription.setMedications(medications);

        // Save the prescription (this should cascade to medications)
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // Update the appointment status to COMPLETED
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED); // Assuming you have an enum for status
        appointmentRepository.save(appointment);

        return convertToPrescriptionDto(savedPrescription);
    }

    @Transactional(readOnly = true) // Use readOnly for read operations
    public PrescriptionDto getPrescriptionByAppointmentId(Long appointmentId) {
        Optional<Prescription> prescription = prescriptionRepository.findByAppointmentId(appointmentId);
        return prescription.map(this::convertToPrescriptionDto).orElse(null);
    }

    // Helper method to convert MedicationDto to Medication entity
    private Medication convertToMedicationEntity(MedicationDto medicationDto, Prescription prescription) {
        Medication medication = new Medication();
        medication.setName(medicationDto.getName());
        medication.setDosage(medicationDto.getDosage());
        medication.setFrequency(medicationDto.getFrequency());
        medication.setDuration(medicationDto.getDuration());
        medication.setPrescription(prescription);
        return medication;
    }

    // Helper method to convert Prescription entity to PrescriptionDto
    private PrescriptionDto convertToPrescriptionDto(Prescription prescription) {
        PrescriptionDto prescriptionDto = new PrescriptionDto();
        prescriptionDto.setId(prescription.getId());
        prescriptionDto.setAppointmentId(prescription.getAppointment().getId());
        prescriptionDto.setDiagnosis(prescription.getDiagnosis());
        prescriptionDto.setNotes(prescription.getNotes());
        prescriptionDto.setDate(prescription.getDate());

        List<MedicationDto> medicationDtos = prescription.getMedications().stream()
                .map(this::convertToMedicationDto)
                .collect(Collectors.toList());

        prescriptionDto.setMedications(medicationDtos);
        return prescriptionDto;
    }

    // Helper method to convert Medication entity to MedicationDto
    private MedicationDto convertToMedicationDto(Medication medication) {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setId(medication.getId());
        medicationDto.setName(medication.getName());
        medicationDto.setDosage(medication.getDosage());
        medicationDto.setFrequency(medication.getFrequency());
        medicationDto.setDuration(medication.getDuration());
        return medicationDto;
    }


}