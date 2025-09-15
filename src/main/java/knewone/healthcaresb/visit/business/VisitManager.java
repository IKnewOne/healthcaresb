package knewone.healthcaresb.visit.business;

import knewone.healthcaresb.doctor.persistence.entity.Doctor;
import knewone.healthcaresb.doctor.persistence.repository.DoctorRepository;
import knewone.healthcaresb.patient.business.dto.DoctorDTO;
import knewone.healthcaresb.patient.business.dto.VisitDTO;
import knewone.healthcaresb.patient.persistence.entity.Patient;
import knewone.healthcaresb.patient.persistence.entity.PatientDoctorData;
import knewone.healthcaresb.patient.persistence.repository.PatientDoctorDataRepository;
import knewone.healthcaresb.patient.persistence.repository.PatientRepository;
import knewone.healthcaresb.visit.communication.dto.CreateVisitRequest;
import knewone.healthcaresb.visit.persistence.entity.Visit;
import knewone.healthcaresb.visit.persistence.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VisitManager {

    private final VisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PatientDoctorDataRepository patientDoctorDataRepository;

    @Transactional // Hibernate deals with race condition by itself
    public VisitDTO createVisit(CreateVisitRequest request) {
        // If the timestamps were already in doctor's local time, we wouldn't need to store timezone data
        // So i assume we are passed UTC and we must convert to local and store and use local everywhere
        // Otherwise it makes no sense
        ZonedDateTime utcStart = request.getStart();
        ZonedDateTime utcEnd = request.getEnd();

        // Therefore we can't avoid extracting doctor prematurely
        // This info should be redis cached with id > zoneid
        Optional<Doctor> doctorOpt = doctorRepository.findById(request.getDoctorId());
        if (doctorOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found");
        }
        Doctor doctor = doctorOpt.get();

        LocalDateTime start = utcStart.withZoneSameInstant(doctor.getTimezone()).toLocalDateTime();
        LocalDateTime end = utcEnd.withZoneSameInstant(doctor.getTimezone()).toLocalDateTime();

        if (!start.isBefore(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time must be before end time");
        }

        // Not needed, don't fetch
        Patient patient = patientRepository.getReferenceById(request.getPatientId());

        // Unavoidable round trip. Business logic requires this out of db
        boolean doctorConflict = visitRepository.isDoubleBooking(doctor.getId(), start, end);
        if (doctorConflict) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor is already booked during this time");
        }

        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setPatient(patient);
        visit.setStartDateTime(start);
        visit.setEndDateTime(end);

        try {
            visit = visitRepository.save(visit);
        } catch (DataIntegrityViolationException e) {
            // This is only the missing patient id right now
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // It's infinitely cheaper to calculate this once instead of doing the math in the search request.
        // Upside is that it's called less often than search too
        Optional<PatientDoctorData> summaryOpt = patientDoctorDataRepository.findByPatientIdAndDoctorId(patient.getId(), doctor.getId());
        if (summaryOpt.isPresent()) {
            PatientDoctorData summary = summaryOpt.get();

            // Update if this is later
            if (summary.getLastVisit() == null || visit.getStartDateTime().isAfter(summary.getLastVisit().getStartDateTime())) {
                summary.setLastVisit(visit);
                patientDoctorDataRepository.save(summary);
            }

        } else {
            PatientDoctorData summary = new PatientDoctorData(patient, doctor, visit);
            patientDoctorDataRepository.save(summary);

            doctor.setTotalPatients(doctor.getTotalPatients() + 1);
            doctor = doctorRepository.save(doctor);
        }

        // These could use mappers and we could use dtos everywhere in place of entities, but it's a demo and only one use soooo
        var doctordto = new DoctorDTO(doctor.getFirstName(), doctor.getLastName(), doctor.getTotalPatients());
        return new VisitDTO(start, end, doctordto);
    }
}
