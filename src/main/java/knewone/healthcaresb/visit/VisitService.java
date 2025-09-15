package knewone.healthcaresb.visit;

import knewone.healthcaresb.patient.business.dto.VisitDTO;
import knewone.healthcaresb.visit.business.VisitManager;
import knewone.healthcaresb.visit.communication.dto.CreateVisitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitManager visitManager;

    public VisitDTO createVisit(CreateVisitRequest request) {
        return visitManager.createVisit(request);
    }
}
