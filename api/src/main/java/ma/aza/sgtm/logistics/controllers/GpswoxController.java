package ma.aza.sgtm.logistics.controllers;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDto;
import ma.aza.sgtm.logistics.services.GpswoxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/gpswox")
@RequiredArgsConstructor
public class GpswoxController {

    private final GpswoxService gpswoxService;

    @GetMapping("/devices")
    public List<DeviceDto> getDevices() {
        return gpswoxService.getDevices();
    }

    @GetMapping("/devices-with-work-hours")
    public List<DeviceDailyHours> getDevicesWithWorkHours(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        return gpswoxService.getDeviceDailyHours(from, to);
    }

}
