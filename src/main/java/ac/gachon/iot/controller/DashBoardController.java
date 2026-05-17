package ac.gachon.iot.controller;

import ac.gachon.iot.dto.DashBoardSummaryResponse;
import ac.gachon.iot.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private final DashBoardService dashBoardService;

    @GetMapping("/summary")
    public DashBoardSummaryResponse getSummary() {
        return dashBoardService.getSummary();
    }
}
