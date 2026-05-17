package ac.gachon.iot.controller;

import ac.gachon.iot.domain.entity.Sensor;
import ac.gachon.iot.dto.SensorFindAllLatestResponse;
import ac.gachon.iot.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/sensor")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @GetMapping("/latest")
    public List<SensorFindAllLatestResponse> getLatestSensors() {
        return sensorService.findAllLatest();
    }

}
