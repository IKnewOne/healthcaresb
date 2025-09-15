package knewone.healthcaresb.patient.business.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    private String search;
    private List<Long> doctorIds;
    private int page = 0;
    private int size = 20;
}
