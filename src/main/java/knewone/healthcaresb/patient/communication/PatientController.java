package knewone.healthcaresb.patient.communication;


import jakarta.validation.constraints.Positive;
import knewone.healthcaresb.patient.PatientService;
import knewone.healthcaresb.patient.business.dto.PatientRequest;
import knewone.healthcaresb.patient.business.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {

    private final PatientService patientService;

    @GetMapping()
    public PatientResponse getPatients(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Long> doctorIds,
            @RequestParam(defaultValue = "1") @Positive int page, // 1-based
            @RequestParam(defaultValue = "20") @Positive int size
    ) {
        PatientRequest request = new PatientRequest(search, doctorIds, page - 1, size);
        return patientService.getPatients(request);
    }
}
