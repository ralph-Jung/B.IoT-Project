package ac.gachon.iot.controller;

import ac.gachon.iot.domain.repository.AlertLogRepository;
import ac.gachon.iot.dto.AlertLogResponse;
import ac.gachon.iot.service.AlertLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class AlertController {

    public final AlertLogService alertLogService;

    @GetMapping("/v1/alerts")
    public List<AlertLogResponse> getAlerts() {
        return alertLogService.findAllAlerts();
    }

}
