package ac.gachon.iot.domain.repository;

import ac.gachon.iot.domain.entity.EnergyDaily;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EnergyDailyRepository extends JpaRepository<EnergyDaily, Long> {

    Optional<EnergyDaily> findByDate(LocalDate date);
}
