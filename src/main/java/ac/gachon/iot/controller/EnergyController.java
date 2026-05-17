package ac.gachon.iot.controller;

import ac.gachon.iot.dto.EnergyDailyResponse;
import ac.gachon.iot.dto.EnergyEfficiencyResponse;
import ac.gachon.iot.service.EnergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/energy")
@RequiredArgsConstructor
public class EnergyController {

    private final EnergyService energyService;

    @GetMapping("/daily")
    public EnergyDailyResponse getDailyEnergy() {
        return energyService.findDailyUsage();
    }

    @GetMapping("/efficiency")
    public EnergyEfficiencyResponse getEnergyEfficiency() {
        return energyService.findTotalMaxWhPerDay();
    }

}
