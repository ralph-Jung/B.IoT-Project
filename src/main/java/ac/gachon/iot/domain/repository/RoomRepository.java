package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    
    @Query("select distinct r from Room r join  fetch r.roomDevices rd join fetch rd.device")
    List<Room> findAllWithDevices();

}
