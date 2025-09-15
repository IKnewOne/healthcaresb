package knewone.healthcaresb.doctor.persistence.repository;

import knewone.healthcaresb.doctor.persistence.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
