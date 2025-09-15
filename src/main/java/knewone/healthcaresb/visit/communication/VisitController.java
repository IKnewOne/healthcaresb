package knewone.healthcaresb.visit.communication;

import jakarta.validation.Valid;
import knewone.healthcaresb.patient.business.dto.VisitDTO;
import knewone.healthcaresb.visit.VisitService;
import knewone.healthcaresb.visit.communication.dto.CreateVisitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/api/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService service;

    @PostMapping("/")
    public VisitDTO createVisit(
            @RequestBody @Valid CreateVisitRequest request
    ) {
        return service.createVisit(request);
    }
}
