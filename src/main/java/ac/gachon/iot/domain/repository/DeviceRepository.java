package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("""
                    select sum(d.powerWatt)
                    from Device d join RoomDevice r on d.id=r.device.id                             
            """)
    Double findTotalMaxWhPerDay();
}
