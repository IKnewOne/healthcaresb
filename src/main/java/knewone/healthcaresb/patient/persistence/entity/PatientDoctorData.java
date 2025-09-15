package knewone.healthcaresb.patient.persistence.entity;

import jakarta.persistence.*;
import knewone.healthcaresb.doctor.persistence.entity.Doctor;
import knewone.healthcaresb.visit.persistence.entity.Visit;
import lombok.*;

@Entity
@Table(name = "patient_doctor_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDoctorData {

    @EmbeddedId
    private PatientDoctorDataId id;

    @MapsId("patientId")
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @MapsId("doctorId")
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "last_visit_id")
    private Visit lastVisit;


    public PatientDoctorData(Patient patient, Doctor doctor, Visit lastVisit) {
        this.patient = patient;
        this.doctor = doctor;
        this.lastVisit = lastVisit;
        this.id = new PatientDoctorDataId(patient.getId(), doctor.getId());
    }
}
