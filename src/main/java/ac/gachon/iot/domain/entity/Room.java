package ac.gachon.iot.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer floor;
}
