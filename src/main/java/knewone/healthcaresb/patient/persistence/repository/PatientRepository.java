package knewone.healthcaresb.patient.persistence.repository;

import knewone.healthcaresb.patient.persistence.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
