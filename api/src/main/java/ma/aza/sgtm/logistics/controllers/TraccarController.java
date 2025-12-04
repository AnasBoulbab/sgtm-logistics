package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.clients.TraccarClient;
import ma.aza.sgtm.logistics.records.ApiResponse;
import ma.aza.sgtm.logistics.records.DeviceDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/traccar")
@RequiredArgsConstructor
public class TraccarController {

    private final TraccarClient traccarClient;

    @GetMapping("/devices")
    public ResponseEntity<ApiResponse<List<DeviceDto>>> getDevices() {

        // Call remote API generically
        ResponseEntity<DeviceDto[]> backendResponse =
                traccarClient.exchange(HttpMethod.GET, "/devices", null, DeviceDto[].class, null);

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
        ResponseEntity<DeviceDto> backendResponse =
                traccarClient.exchange(HttpMethod.POST, "/some-resource", requestDto, DeviceDto.class, null);

        if (!backendResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity
                    .status(backendResponse.getStatusCode())
                    .body(ApiResponse.error("Failed to create resource on remote server"));
        }

        return ResponseEntity
                .status(backendResponse.getStatusCode()) // e.g. 201 CREATED if remote returns it
                .body(ApiResponse.ok(backendResponse.getBody()));
    }
}
