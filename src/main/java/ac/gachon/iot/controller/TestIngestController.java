package ac.gachon.iot.controller;

import ac.gachon.iot.dto.MqttSensorPayload;
import ac.gachon.iot.service.SensorDataIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Profile("!prod")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestIngestController {

    private final SensorDataIngestionService sensorDataIngestionService;

    @PostMapping("/ingest/{identifier}")
    public void ingest(
            @PathVariable String identifier,
            @RequestParam(defaultValue = "25.0") BigDecimal temperature,
            @RequestParam(defaultValue = "50.0") BigDecimal humidity,
            @RequestParam(defaultValue = "0") Short motion
    ) {
        sensorDataIngestionService.ingest(identifier, new MqttSensorPayload(temperature, humidity, motion));
    }
}
