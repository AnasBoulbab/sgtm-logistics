package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.records.ApiResponse;
import ma.aza.sgtm.logistics.records.DeviceDto;
import ma.aza.sgtm.logistics.services.GPSService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/traccar")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "gps-provider", name = "name", havingValue = "traccar")
public class TraccarController {

    private final GPSService gpsService;

    @GetMapping("/devices")
    public ResponseEntity<ApiResponse<List<DeviceDto>>> getDevices() {

        // Call remote API generically
        ResponseEntity<DeviceDto[]> backendResponse =
                ResponseEntity.ok(gpsService.getDevices().stream()
                        .map(device -> new ma.aza.sgtm.logistics.records.DeviceDto(device.getId().intValue(), device.getName(), null, null, null))
                        .toArray(ma.aza.sgtm.logistics.records.DeviceDto[]::new));

        if (!backendResponse.getStatusCode().is2xxSuccessful()) {
            // Propagate status code, wrap error
            return ResponseEntity
                    .status(backendResponse.getStatusCode())
                    .body(ApiResponse.error("Failed to fetch devices from remote server"));
        }

        DeviceDto[] body = backendResponse.getBody();
        List<DeviceDto> devices = body != null ? Arrays.asList(body) : List.of();

        return ResponseEntity
                .status(backendResponse.getStatusCode())  // same as remote (e.g. 200)
                .body(ApiResponse.ok(devices));
    }

    @PostMapping("/some-resource")
    public ResponseEntity<ApiResponse<DeviceDto>> createSomething(@RequestBody DeviceDto requestDto) {
        throw new UnsupportedOperationException("Creating resources is not supported through the generic GPSService");
    }
}
