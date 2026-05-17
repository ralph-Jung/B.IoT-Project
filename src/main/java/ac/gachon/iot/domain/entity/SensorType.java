package ac.gachon.iot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "sensor_type")
@Getter
public class SensorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
