package ac.gachon.iot.domain.entity;

import ac.gachon.iot.domain.enums.SensorStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    private BigDecimal temperature;

    private BigDecimal humidity;

    private Short motion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorStatus status;

    @CreatedDate
    private OffsetDateTime createdAt;


}

