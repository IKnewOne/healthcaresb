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
public class PatientJdbcRepositoryImpl implements PatientJdbcRepository {

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

        // count
        String countSql = """
                    SELECT COUNT(DISTINCT p.id)
                    FROM patient p
                    JOIN visit v ON v.patient_id = p.id
                    JOIN doctor d ON d.id = v.doctor_id
                """ + where;

        Long count = jdbcTemplate.queryForObject(countSql, params, Long.class);
        if (count == 0) return new PatientResponse(List.of(), 0);


        String sql = """
                    -- Broken pagination if we start anywhere else
                    WITH filtered_patients AS (
                        SELECT p.id
                        FROM patient p
                        JOIN visit v ON v.patient_id = p.id
                        JOIN doctor d ON d.id = v.doctor_id
                """ + where + """
                    GROUP BY p.id
                    ORDER BY p.first_name, p.last_name
                    LIMIT :limit OFFSET :offset
                ),
                ranked_visits AS (
                    SELECT v.*,
                           ROW_NUMBER() OVER (
                               PARTITION BY v.patient_id, v.doctor_id
                               ORDER BY v.start_date_time DESC, v.id DESC
                           ) AS rn
                    FROM visit v
                ),
                doctor_patient_counts AS (
                    SELECT doctor_id, COUNT(DISTINCT patient_id) AS total_patients
                    FROM visit
                    GROUP BY doctor_id
                )
                SELECT p.id AS patient_id,
                       p.first_name AS patient_first,
                       p.last_name AS patient_last,
                       rv.id AS visit_id,
                       rv.start_date_time AS visit_start,
                       rv.end_date_time AS visit_end,
                       d.id AS doctor_id,
                       d.first_name AS doctor_first,
                       d.last_name AS doctor_last,
                       dpc.total_patients
                FROM filtered_patients fp
                JOIN patient p ON p.id = fp.id
                JOIN ranked_visits rv ON rv.patient_id = p.id AND rv.rn = 1
                JOIN doctor d ON d.id = rv.doctor_id
                JOIN doctor_patient_counts dpc ON d.id = dpc.doctor_id
                """ + where + """
                    ORDER BY p.first_name, p.last_name
                """;

        params.addValue("limit", request.getSize());
        params.addValue("offset", request.getPage() * request.getSize());

        // 3️⃣ Map rows to PatientDTO with aggregated visits
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
                    (Long) row.get("total_patients")
            );

            VisitDTO visitDTO = new VisitDTO(
                    (LocalDateTime) row.get("visit_start"),
                    (LocalDateTime) row.get("visit_end"),
                    doctorDTO
            );

            patientDTO.getLastVisits().add(visitDTO);
        }

        return new PatientResponse(new ArrayList<>(patientMap.values()), count);
    }
}
