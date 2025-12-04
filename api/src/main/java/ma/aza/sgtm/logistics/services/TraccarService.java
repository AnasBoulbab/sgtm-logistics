package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.clients.TraccarClient;
import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDto;
import ma.aza.sgtm.logistics.records.DateTimeRange;
import ma.aza.sgtm.logistics.records.PositionDto;
import ma.aza.sgtm.logistics.utils.DateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "gps-provider", name = "name", havingValue = "traccar")
public class TraccarService implements GPSService {

    private final TraccarClient traccarClient;

    @Override
    public List<DeviceDto> getDevices() {
        ResponseEntity<ma.aza.sgtm.logistics.records.DeviceDto[]> backendResponse = traccarClient.exchange(
                HttpMethod.GET,
                "/devices",
                null,
                null,
                ma.aza.sgtm.logistics.records.DeviceDto[].class
        );
        if (!backendResponse.getStatusCode().is2xxSuccessful()) {
            return List.of();
        }
        ma.aza.sgtm.logistics.records.DeviceDto[] body = backendResponse.getBody();
        if (body == null) {
            return List.of();
        }

        return List.of(body).stream()
                .map(device -> new DeviceDto(Long.valueOf(device.id()), device.name()))
                .toList();
    }

    @Override
    public String getEngineHours(Long id, LocalDateTime from, LocalDateTime to) {
        throw new UnsupportedOperationException("Engine hours not supported for Traccar provider");
    }

    @Override
    public List<DeviceDailyHours> getDeviceDailyHours(LocalDateTime from, LocalDateTime to) {
        return getDevicesWithEfficacity(from, to);
    }

    public List<PositionDto> getPositions(Integer deviceId, LocalDateTime from, LocalDateTime to, Integer id) {
        HashMap<String, String> queryParams = new HashMap<>();
        if (deviceId != null) queryParams.put("deviceId", String.valueOf(deviceId));
        if (from != null) queryParams.put("from", from.toString());
        if (to != null) queryParams.put("to", to.toString());
        if (id != null) queryParams.put("id", String.valueOf(deviceId));
        ResponseEntity<PositionDto[]> backendResponse = traccarClient.exchange(
                HttpMethod.GET,
                "/positions",
                queryParams,
                null,
                PositionDto[].class
        );
        if (!backendResponse.getStatusCode().is2xxSuccessful()) {
            return List.of();
        }
        PositionDto[] body = backendResponse.getBody();
        return body == null ? List.of() : List.of(body);
    }

    public DeviceDailyHours getDeviceWithEfficacity(DeviceDto device, LocalDateTime from, LocalDateTime to) {
        DeviceDailyHours deviceDailyHours = new DeviceDailyHours();
        deviceDailyHours.setId(device.getId());
        deviceDailyHours.setName(device.getName());
        List<DateTimeRange> dateTimeRanges = DateUtil.getDayTimeRanges(from.toLocalDate(), to.toLocalDate(), from.toLocalTime(), to.toLocalTime());
        for (DateTimeRange dateTimeRange : dateTimeRanges) {
            List<PositionDto> positions = getPositions(device.getId().intValue(), dateTimeRange.start(), dateTimeRange.end(), null);
            // TODO : Go over all positions of each day and calculate the difference between start and end io449
        }
        return deviceDailyHours;
    }

    public List<DeviceDailyHours> getDevicesWithEfficacity(LocalDateTime from, LocalDateTime to) {
        List<DeviceDailyHours> deviceDailyHours = new ArrayList<>();
        List<DeviceDto> devices = getDevices();
        for (DeviceDto device : devices) {
            deviceDailyHours.add(getDeviceWithEfficacity(device, from, to));
        }
        return deviceDailyHours;
    }
}
