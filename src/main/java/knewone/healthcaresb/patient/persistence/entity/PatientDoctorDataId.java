package knewone.healthcaresb.patient.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDoctorDataId implements Serializable {
    private Long patientId;
    private Long doctorId;
}
