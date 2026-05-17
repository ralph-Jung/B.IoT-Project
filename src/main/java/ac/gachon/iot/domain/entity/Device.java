package ac.gachon.iot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer powerWatt;

    @Column(nullable = false)
    private OffsetDateTime createdAt;
}
