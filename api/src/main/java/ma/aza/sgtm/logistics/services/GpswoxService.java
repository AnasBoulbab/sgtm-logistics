package ma.aza.sgtm.logistics.services;

import lombok.RequiredArgsConstructor;
import ma.aza.sgtm.logistics.clients.GpswoxClient;
import ma.aza.sgtm.logistics.dtos.DailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDto;
import ma.aza.sgtm.logistics.records.DateTimeRange;
import ma.aza.sgtm.logistics.records.DeviceGroup;
import ma.aza.sgtm.logistics.records.GetHistoryResponse;
import ma.aza.sgtm.logistics.utils.DateUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GpswoxService {

    private final GpswoxClient gpswoxClient;

    public List<DeviceDto> getDevices() {
        // Call your client
        ResponseEntity<DeviceGroup[]> response =
                gpswoxClient.getDevices(DeviceGroup[].class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            // up to you: throw, log, or just return empty
            return Collections.emptyList();
        }

        DeviceGroup[] body = response.getBody();
        if (body == null || body.length == 0) {
            return Collections.emptyList();
        }

        List<DeviceDto> result = new ArrayList<>();

        Arrays.stream(body).forEach(group -> {
            if (group.items() != null) {
                group.items().forEach(item ->
                        result.add(new DeviceDto(item.id(), item.name()))
                );
            }
        });

        return result;
    }

    public String getEngineHours(Long id, LocalDateTime from, LocalDateTime to) {

        ResponseEntity<GetHistoryResponse> response = gpswoxClient.getHistory(id, from, to, GetHistoryResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Failed to fetch history for device " + id + ", status = " + response.getStatusCode());
        }

        GetHistoryResponse body = response.getBody();
        if (body == null || body.engineHours() == null) {
            // You can return null, Optional, or throw – up to your design.
            throw new IllegalStateException("No engine_hours returned for device " + id);
        }

        return body.engineHours();
    }

    public List<DeviceDailyHours> getDeviceDailyHours(LocalDateTime from, LocalDateTime to) {

        List<DeviceDto> devices = getDevices();

        if (devices.isEmpty()) {
            return Collections.emptyList();
        }

        List<DeviceDailyHours> result = new ArrayList<>();

        List<DateTimeRange> ranges = DateUtil.getDayTimeRanges(from.toLocalDate(), to.toLocalDate(), from.toLocalTime(), to.toLocalTime());

        for (DeviceDto device : devices) {

            DeviceDailyHours deviceDailyHours = new DeviceDailyHours();
            List<DailyHours> currentDeviceDailyHours = new ArrayList<>();

            deviceDailyHours.setId(device.getId());
            deviceDailyHours.setName(device.getName());

            for (DateTimeRange range : ranges) {

                String engineHours = getEngineHours(device.getId(), range.start(), range.end());

                DailyHours dailyHours = new DailyHours();

                dailyHours.setDay(range.start().toLocalDate());
                dailyHours.setHours(engineHours);

                currentDeviceDailyHours.add(dailyHours);

            }

            deviceDailyHours.setDaysHour(currentDeviceDailyHours);

            result.add(deviceDailyHours);
        }

        return result;

    }

}
