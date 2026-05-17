package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.RoomDevice;
import ac.gachon.iot.domain.enums.DeviceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDeviceRepository extends JpaRepository<RoomDevice, Long> {

    @Query("""
            select sum(d.powerWatt)
            from RoomDevice r join r.device d
            where r.status=:status
            """)
    Long findCurrentTotalWatt(@Param("status") DeviceStatus status);
}
