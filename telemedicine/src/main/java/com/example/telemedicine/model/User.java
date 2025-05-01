    package com.example.telemedicine.model;

    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import java.time.LocalDate;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true, length = 50)
        private String username;

        @Column(nullable = false, unique = true, length = 100)
        private String email;

        @Column(nullable = false, length = 255)
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 10)
        private Role role;

        @Column(length = 15)
        private String phoneNumber;

        private LocalDate dateOfBirth;

        @Enumerated(EnumType.STRING)
        @Column(length = 10)
        private Gender gender;

        @Column(length = 100)
        private String specialization;

        @Column(length = 50)
        private String licenseNumber;

        @Column(length = 255)
        private String yearsOfExperience;

        @Column(columnDefinition = "TEXT")
        private String aboutMe;

        @Column(length = 50)
        private String country;

        private String profilePictureUrl;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, length = 10)
        private Status status = Status.ACTIVE;

        @CreationTimestamp
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(nullable = false)
        private LocalDateTime updatedAt;

        public enum Role {
            PATIENT, DOCTOR, ADMIN
        }

        public enum Gender {
            MALE, FEMALE, OTHER, PREFER_NOT_TO_SAY
        }

        public enum Status {
            ACTIVE, INACTIVE
        }
    }