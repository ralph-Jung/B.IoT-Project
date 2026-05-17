package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.Sensor;
import ac.gachon.iot.domain.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
//    List<SensorData> findTopBySensorOrderByCreatedAt();
}
