package com.jade.detect.service;

import com.jade.detect.model.Device;
import com.jade.detect.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    public Optional<Device> getDeviceByName(String name) {
        return Optional.ofNullable(deviceRepository.findByName(name));
    }

    public List<Device> getDevicesByLocation(String location) {
        return deviceRepository.findByLocation(location);
    }

    public Device createDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Optional<Device> updateDevicePartial(Long id, Map<String, Object> updates) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        if (optionalDevice.isEmpty()) {
            return Optional.empty();
        }

        Device device = optionalDevice.get();

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> device.setName((String) value);
                case "type" -> device.setType((String) value);
                case "location" -> device.setLocation((String) value);
                case "status" -> device.setStatus(Device.DeviceStatus.valueOf(value.toString()));
            }
        });

        Device saved = deviceRepository.save(device);
        return Optional.of(saved);
    }


    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}
