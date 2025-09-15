package knewone.healthcaresb.patient.business.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private List<PatientDTO> data;
    private long count;


}

