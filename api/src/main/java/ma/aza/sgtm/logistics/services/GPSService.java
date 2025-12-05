package ma.aza.sgtm.logistics.services;

import ma.aza.sgtm.logistics.dtos.DeviceDailyHours;
import ma.aza.sgtm.logistics.dtos.DeviceDto;

import java.time.LocalDateTime;
import java.util.List;

public interface GPSService {

    List<DeviceDto> getDevices();
    String getEngineHours(Long id, LocalDateTime from, LocalDateTime to);
    List<DeviceDailyHours> getDeviceDailyHours(LocalDateTime from, LocalDateTime to);

}
