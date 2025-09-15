package knewone.healthcaresb.patient.persistence.repository;

import knewone.healthcaresb.patient.business.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PatientJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PatientResponse getPatients(PatientRequest request) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        if (request.getSearch() != null && !request.getSearch().isBlank()) {
            where.append(" AND LOWER(CONCAT(p.first_name, ' ', p.last_name)) LIKE :search");
            params.addValue("search", "%" + request.getSearch().toLowerCase() + "%");
        }

        if (request.getDoctorIds() != null && !request.getDoctorIds().isEmpty()) {
            where.append(" AND d.id IN (:doctorIds)");
            params.addValue("doctorIds", request.getDoctorIds());
        }

        // Count query
        String countSql = """
                    SELECT COUNT(*)
                    FROM patient_doctor_data pds
                    JOIN patient p ON p.id = pds.patient_id
                    JOIN doctor d ON d.id = pds.doctor_id
                """ + where;

        Long count = jdbcTemplate.queryForObject(countSql, params, Long.class);
        if (count == 0) return new PatientResponse(List.of(), 0);

        // Data query
        String sql = """
                    SELECT p.id AS patient_id,
                           p.first_name AS patient_first,
                           p.last_name AS patient_last,
                           v.id AS visit_id,
                           v.start_date_time AS visit_start,
                           v.end_date_time AS visit_end,
                           d.id AS doctor_id,
                           d.first_name AS doctor_first,
                           d.last_name AS doctor_last,
                           d.total_patients
                    FROM patient_doctor_data pds
                    JOIN patient p ON p.id = pds.patient_id
                    JOIN visit v ON v.id = pds.last_visit_id
                    JOIN doctor d ON d.id = pds.doctor_id
                """ + where +
                " ORDER BY p.first_name, p.last_name LIMIT :limit OFFSET :offset";

        params.addValue("limit", request.getSize());
        params.addValue("offset", request.getPage() * request.getSize());

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);

        Map<Long, PatientDTO> patientMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            Long patientId = ((Number) row.get("patient_id")).longValue();

            PatientDTO patientDTO = patientMap.computeIfAbsent(patientId, id -> new PatientDTO(
                    (String) row.get("patient_first"),
                    (String) row.get("patient_last"),
                    new ArrayList<>()
            ));

            DoctorDTO doctorDTO = new DoctorDTO(
                    (String) row.get("doctor_first"),
                    (String) row.get("doctor_last"),
                    ((Number) row.get("total_patients")).longValue()
            );

            VisitDTO visitDTO = new VisitDTO(
                    ((LocalDateTime) row.get("visit_start")),
                    ((LocalDateTime) row.get("visit_end")),
                    doctorDTO
            );

            patientDTO.getLastVisits().add(visitDTO);
        }

        return new PatientResponse(new ArrayList<>(patientMap.values()), count);
    }

}
