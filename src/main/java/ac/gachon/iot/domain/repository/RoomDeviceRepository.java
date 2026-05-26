package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.Device;
import ac.gachon.iot.domain.entity.Room;
import ac.gachon.iot.domain.entity.RoomDevice;
import ac.gachon.iot.domain.entity.RoomDeviceId;
import ac.gachon.iot.domain.enums.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomDeviceRepository extends JpaRepository<RoomDevice, RoomDeviceId> {

    @Query("""
            select sum(d.powerWatt)
            from RoomDevice r join r.device d
            where r.status=:status
            """)
    Long findCurrentTotalWatt(@Param("status") DeviceStatus status);

    Optional<RoomDevice> findByRoomAndDevice(Room room, Device device);

    @Query("SELECT rd FROM RoomDevice rd JOIN FETCH rd.device WHERE rd.room.id = :roomId")
    List<RoomDevice> findByRoomId(@Param("roomId") Long roomId);
}
