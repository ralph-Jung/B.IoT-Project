package ac.gachon.iot.service;

import ac.gachon.iot.domain.repository.AlertLogRepository;
import ac.gachon.iot.dto.AlertLogResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertLogService {

    private final AlertLogRepository alertLogRepository;

    public List<AlertLogResponse> findAllAlerts() {
        List<AlertLogResponse> list = alertLogRepository.findALlAlerts();
        return alertLogRepository.findALlAlerts();
    }
}
