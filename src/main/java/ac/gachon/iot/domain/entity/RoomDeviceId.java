package ac.gachon.iot.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class RoomDeviceId implements Serializable {

    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "device_id")
    private Long deviceId;
}
