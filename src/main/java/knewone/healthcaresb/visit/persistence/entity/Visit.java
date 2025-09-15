package knewone.healthcaresb.visit.persistence.entity;

import jakarta.persistence.*;
import knewone.healthcaresb.doctor.persistence.entity.Doctor;
import knewone.healthcaresb.patient.persistence.entity.Patient;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
