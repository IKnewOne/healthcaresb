package knewone.healthcaresb.patient.persistence.repository;

import knewone.healthcaresb.patient.business.dto.PatientRequest;
import knewone.healthcaresb.patient.business.dto.PatientResponse;

public interface PatientJdbcRepository {
    PatientResponse getPatients(PatientRequest request);
}
