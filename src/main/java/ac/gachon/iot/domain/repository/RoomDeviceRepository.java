package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.RoomDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDeviceRepository extends JpaRepository<RoomDevice, Long> {
}
