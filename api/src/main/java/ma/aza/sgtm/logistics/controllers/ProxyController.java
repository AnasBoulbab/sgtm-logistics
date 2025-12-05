package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDto;
import ma.aza.sgtm.logistics.services.GPSService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/proxy")
@RequiredArgsConstructor
public class ProxyController {

    private final GPSService gpsService;

    @GetMapping("/devices")
    public List<DeviceDto> getDevices() {
        return gpsService.getDevices();
    }

    @GetMapping("/devices-with-work-hours")
    public List<DeviceDailyHours> getDevicesWithWorkHours(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        return gpsService.getDeviceDailyHours(from, to);
    }

}
