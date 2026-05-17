package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.ControlLog;
import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.dto.ControlLogResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ControlLogRepository extends JpaRepository<ControlLog, Long> {
    @Query("SELECT c FROM ControlLog c JOIN FETCH c.room JOIN FETCH c.device WHERE c.createdAt BETWEEN :start AND :end")
    List<ControlLog> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);

    Optional<ControlLog> findTopByRoomAndDeviceOrderByCreatedAtDesc(Room room, Device device);

    @Query("""
            SELECT a
            FROM ControlLog as a JOIN FETCH a.room JOIN FETCH a.device
            """)
    List<ControlLog> findAllControlLog();


}
