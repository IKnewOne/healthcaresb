package knewone.healthcaresb.visit.persistence.repository;

import knewone.healthcaresb.visit.persistence.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
            FROM Visit v
            WHERE v.doctor.id = :doctorId
              AND v.startDateTime < :end
              AND v.endDateTime > :start
            """)
    boolean isDoubleBooking(@Param("doctorId") long doctorId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
