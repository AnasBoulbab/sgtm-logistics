package ma.aza.sgtm.logistics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDto;
import ma.aza.sgtm.logistics.services.GPSService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "GPS Proxy", description = "Expose data fetched from the configured GPS provider")
public class ProxyController {

    private final GPSService gpsService;

    @GetMapping("/devices")
    @Operation(summary = "List devices", description = "Retrieve all devices from the configured GPS provider")
    public List<DeviceDto> getDevices() {
        return gpsService.getDevices();
    }

    @GetMapping("/devices-with-work-hours")
    @Operation(
            summary = "Device activity over time",
            description = "Return device day-by-day work hours calculated from the GPS provider",
            parameters = {
                    @Parameter(name = "from", description = "Start of the interval (inclusive)", required = true),
                    @Parameter(name = "to", description = "End of the interval (inclusive)", required = true)
            }
    )
    public List<DeviceDailyHours> getDevicesWithWorkHours(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        return gpsService.getDeviceDailyHours(from, to);
    }

}
