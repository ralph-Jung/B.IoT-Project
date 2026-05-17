package ac.gachon.iot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sensor_type_id")
    private SensorType sensorType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    private String name;
}
