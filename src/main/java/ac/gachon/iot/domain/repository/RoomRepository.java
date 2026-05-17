package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
}
