package ac.gachon.iot.service;

import ac.gachon.iot.domain.entity.SensorData;
import ac.gachon.iot.domain.repository.SensorDataRepository;
import ac.gachon.iot.dto.SensorFindAllLatestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorDataRepository sensorDataRepository;

    public List<SensorFindAllLatestResponse> findAllLatest() {

        List<SensorData> sensorData = sensorDataRepository.findAllLatest();

        return sensorData.stream().map(s -> SensorFindAllLatestResponse.builder()
                .sensorId(s.getSensor().getId())
                .temperature(s.getTemperature())
                .humidity(s.getHumidity())
                .motion(s.getMotion())
                .status(s.getStatus())
                .build()).toList();

    }
}
