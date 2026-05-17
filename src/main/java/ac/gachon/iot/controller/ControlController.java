package ac.gachon.iot.controller;

import ac.gachon.iot.dto.ControlLogResponse;
import ac.gachon.iot.dto.CreateControlLogRequest;
import ac.gachon.iot.service.ControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/control")
@RequiredArgsConstructor
public class ControlController {

    private final ControlService controlService;

    @GetMapping("/logs")
    public List<ControlLogResponse> getControlLogs() {
        return controlService.findAllControlLogs();

    }

    @Operation
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회성공"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/404"),
            @ApiResponse(responseCode = "403", description = "권한 없음")
    })
    @PostMapping("")
    public void postControlLog(@RequestBody CreateControlLogRequest request) {
        controlService.createControlLog(request);
    }

}
