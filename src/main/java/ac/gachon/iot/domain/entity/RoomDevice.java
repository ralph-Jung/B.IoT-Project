package ac.gachon.iot.domain.entity;

import ac.gachon.iot.domain.enums.ControlMode;
import ac.gachon.iot.domain.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;


@Entity
@Getter
@Table(name = "room_device")
public class RoomDevice {

    @EmbeddedId
    private RoomDeviceId id;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id")
    private Room room;

    @MapsId("deviceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ControlMode mode;

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
