package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    @Query("select s from SensorData s where s.createdAt=(select max(s2.createdAt) from SensorData s2 where s2.sensor=s.sensor)")
    List<SensorData> findAllLatest();

}

