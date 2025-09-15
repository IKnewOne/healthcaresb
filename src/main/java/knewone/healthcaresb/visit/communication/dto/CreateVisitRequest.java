package knewone.healthcaresb.visit.communication.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CreateVisitRequest {
    private ZonedDateTime start;
    private ZonedDateTime end;
    @Positive
    private long patientId;
    @Positive
    private long doctorId;
}
