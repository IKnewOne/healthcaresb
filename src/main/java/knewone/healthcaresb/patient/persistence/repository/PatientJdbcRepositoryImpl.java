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
        String sql = """
                WITH filtered_patients AS (
                        SELECT DISTINCT p.id as patient_id, p.first_name, p.last_name, d.id as doctor_id
                        FROM patient p
                        JOIN visit v ON v.patient_id = p.id
                        JOIN doctor d ON d.id = v.doctor_id
                """ + where + """
                    ORDER BY p.first_name, p.last_name
                ),
                paginated_patients AS (
                    SELECT DISTINCT fp.patient_id
                    FROM filtered_patients fp
                    LIMIT :limit OFFSET :offset
                ),
                paginated_doctors AS (
                    SELECT DISTINCT fp.doctor_id
                    FROM filtered_patients fp
                    LIMIT :limit OFFSET :offset
                ),
                total_count AS (
                     SELECT COUNT(*) AS cnt
                     FROM (SELECT DISTINCT patient_id FROM filtered_patients) up
                ),
                ranked_visits AS (
                    SELECT v.*,
                           ROW_NUMBER() OVER (
                               PARTITION BY v.patient_id, v.doctor_id
                               ORDER BY v.start_date_time DESC, v.id DESC
                           ) AS rn
                    FROM visit v
                    WHERE v.patient_id IN (SELECT DISTINCT patient_id FROM paginated_patients)
                ),
                doctor_patient_counts AS (
                    SELECT doctor_id, COUNT(DISTINCT patient_id) AS total_patients
                    FROM visit v
                    WHERE v.doctor_id IN (SELECT DISTINCT doctor_id FROM paginated_doctors)
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
                       dpc.total_patients,
                       tc.cnt AS total_count
                FROM paginated_patients pp
                JOIN patient p ON p.id = pp.patient_id
                JOIN ranked_visits rv ON rv.patient_id = pp.patient_id AND rv.rn = 1
                JOIN doctor d ON d.id = rv.doctor_id
                JOIN doctor_patient_counts dpc ON d.id = dpc.doctor_id
                CROSS JOIN total_count tc
                """ + where;

        params.addValue("limit", request.getSize());
        params.addValue("offset", request.getPage() * request.getSize());

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);

        long totalCount = 0;

        Map<Long, PatientDTO> patientMap = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            if (totalCount == 0) { totalCount = (long) row.get("total_count"); }

            Long patientId = (Long) row.get("patient_id");

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

        return new PatientResponse(new ArrayList<>(patientMap.values()), totalCount);
    }
}
