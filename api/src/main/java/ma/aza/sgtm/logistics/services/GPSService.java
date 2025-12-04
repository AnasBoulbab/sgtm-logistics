package ma.aza.sgtm.logistics.services;

import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.records.DeviceDto;

import java.time.LocalDateTime;
import java.util.List;

public interface GPSService {

    DeviceDto getDevice(Long id);
    List<DeviceDto> getDevices();
    DeviceDailyHours getDeviceWithWorkHours(Long id, LocalDateTime from, LocalDateTime to);
    List<DeviceDailyHours> getDevicesWithWorkHours(LocalDateTime from, LocalDateTime to);

}
