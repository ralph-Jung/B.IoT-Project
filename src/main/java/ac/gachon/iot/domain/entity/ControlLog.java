package ac.gachon.iot.domain.entity;

import ac.gachon.iot.domain.enums.ControlMode;
import ac.gachon.iot.domain.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "control_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ControlMode type;

    @CreationTimestamp
    private OffsetDateTime createdAt = OffsetDateTime.now();

}




