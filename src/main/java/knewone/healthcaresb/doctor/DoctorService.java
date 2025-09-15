package knewone.healthcaresb.doctor;

import knewone.healthcaresb.doctor.persistence.entity.Doctor;
import knewone.healthcaresb.doctor.persistence.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DoctorService {
    private final DoctorRepository repository;

    public Doctor save(Doctor doctor) {
        return this.repository.save(doctor);
    }
}
