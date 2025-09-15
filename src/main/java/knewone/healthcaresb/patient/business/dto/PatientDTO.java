package knewone.healthcaresb.patient.business.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private String firstName;
    private String lastName;
    private List<VisitDTO> lastVisits;
}
