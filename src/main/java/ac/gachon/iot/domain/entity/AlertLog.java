package ac.gachon.iot.domain.entity;

import ac.gachon.iot.domain.enums.AlertType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "alert_log")
public class AlertLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @Enumerated(EnumType.STRING)
    private AlertType type;

    private String message;

    private OffsetDateTime createdAt = OffsetDateTime.now();

}
