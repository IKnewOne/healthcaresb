package knewone.healthcaresb.patient.business.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitDTO {
    private LocalDateTime start;
    private LocalDateTime end;
    private DoctorDTO doctor;
}
