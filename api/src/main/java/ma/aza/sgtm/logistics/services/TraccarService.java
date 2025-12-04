package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.clients.TraccarClient;
import ma.aza.sgtm.logistics.records.DateTimeRange;
import ma.aza.sgtm.logistics.records.DeviceDto;
import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.records.PositionDto;
import ma.aza.sgtm.logistics.utils.DateUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TraccarService {

    private TraccarClient traccarClient;

    public List<DeviceDto> getDevices() {
        // TODO : get the current connected client
        ResponseEntity<DeviceDto[]> backendResponse = traccarClient.exchange(HttpMethod.GET, "/devices", null, DeviceDto[].class, null);
        if (!backendResponse.getStatusCode().is2xxSuccessful()) {
            // Propagate status code, wrap error
            return List.of();
        }
        DeviceDto[] body = backendResponse.getBody();
        return body == null ? List.of() : List.of(body);
    }

    public List<PositionDto> getPositions(Integer deviceId, LocalDateTime from, LocalDateTime to, Integer id) {
        // TODO : get the current connected client
        HashMap<String, String> queryParams = new HashMap<>();
        if (deviceId != null) queryParams.put("deviceId", String.valueOf(deviceId));
        if (from != null) queryParams.put("from", from.toString());
        if (to != null) queryParams.put("to", to.toString());
        if (id != null) queryParams.put("id", String.valueOf(deviceId));
        ResponseEntity<PositionDto[]> backendResponse = traccarClient.exchange(HttpMethod.GET, "/positions", null, PositionDto[].class, queryParams);
        if (!backendResponse.getStatusCode().is2xxSuccessful()) {
            // Propagate status code, wrap error
            return List.of();
        }
        PositionDto[] body = backendResponse.getBody();
        return body == null ? List.of() : List.of(body);
    }

    public DeviceDailyHours getDeviceWithEfficacity(DeviceDto device, LocalDateTime from, LocalDateTime to) {
        DeviceDailyHours deviceDailyHours = new DeviceDailyHours();
        deviceDailyHours.setId(Long.valueOf(device.id()));
        deviceDailyHours.setName(device.name());
        List<DateTimeRange> dateTimeRanges = DateUtil.getDayTimeRanges(from.toLocalDate(), to.toLocalDate(), from.toLocalTime(), to.toLocalTime());
        for (DateTimeRange dateTimeRange : dateTimeRanges) {
            List<PositionDto> positions = getPositions(device.id(), dateTimeRange.start(), dateTimeRange.end(), null);
            // TODO : Go over all positions of each day and calculate the difference between start and end io449
        }
        return deviceDailyHours;
    }

    public List<DeviceDailyHours> getDevicesWithEfficacity(LocalDateTime from, LocalDateTime to) {
        List<DeviceDailyHours> deviceDailyHours = new ArrayList<>();
        List<DeviceDto> devices = getDevices();
        List<DateTimeRange> dateTimeRanges = DateUtil.getDayTimeRanges(from.toLocalDate(), to.toLocalDate(), from.toLocalTime(), to.toLocalTime());
        for (DeviceDto device : devices) {
            deviceDailyHours.add(getDeviceWithEfficacity(device, from, to));
        }
        return deviceDailyHours;
    }

}
