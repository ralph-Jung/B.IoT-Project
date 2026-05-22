package ac.gachon.iot.domain.entity;

import ac.gachon.iot.domain.enums.SensorStatus;
import ac.gachon.iot.dto.MqttSensorPayload;
import jakarta.persistence.*;
import lombok.Getter;

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

    private OffsetDateTime createdAt;

    public static SensorData create(Sensor sensor, MqttSensorPayload payload, SensorStatus status) {
        SensorData data = new SensorData();
        data.sensor = sensor;
        data.temperature = payload.temperature();
        data.humidity = payload.humidity();
        data.motion = payload.motion();
        data.status = status;
        data.createdAt = OffsetDateTime.now();
        return data;
    }
}

