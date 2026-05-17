package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.AlertLog;
import ac.gachon.iot.dto.AlertLogResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {

    @Query("""
            select new ac.gachon.iot.dto.AlertLogResponse(a.id,a.type,a.message,a.createdAt,s.name)
                        from AlertLog a join a.sensor s join s.room r
            """)
    List<AlertLogResponse> findALlAlerts();

    long countByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);

}
