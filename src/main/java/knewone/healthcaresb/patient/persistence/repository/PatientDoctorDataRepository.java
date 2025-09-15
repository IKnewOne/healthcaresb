package knewone.healthcaresb.patient.persistence.repository;

import knewone.healthcaresb.patient.persistence.entity.PatientDoctorData;
import knewone.healthcaresb.patient.persistence.entity.PatientDoctorDataId;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PatientDoctorDataRepository extends CrudRepository<PatientDoctorData, PatientDoctorDataId> {

    Optional<PatientDoctorData> findByPatientIdAndDoctorId(long patientId, long doctorId);
}
