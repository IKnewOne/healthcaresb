package knewone.healthcaresb.patient;

import knewone.healthcaresb.patient.business.dto.PatientRequest;
import knewone.healthcaresb.patient.business.dto.PatientResponse;
import knewone.healthcaresb.patient.persistence.entity.Patient;
import knewone.healthcaresb.patient.persistence.repository.PatientJdbcRepository;
import knewone.healthcaresb.patient.persistence.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientJdbcRepository patientJdbcRepository;
    private final PatientRepository repository;

    public PatientResponse getPatients(PatientRequest request) {
        return patientJdbcRepository.getPatients(request);
    }

    public Patient save(Patient patient) {
        return repository.save(patient);
    }

}
