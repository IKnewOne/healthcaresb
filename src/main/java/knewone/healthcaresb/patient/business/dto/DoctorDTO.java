package knewone.healthcaresb.patient.business.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private String firstName;
    private String lastName;
    private Long totalPatients;
}
