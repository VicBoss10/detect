package com.jade.detect.controller;

import com.jade.detect.model.Device;
import com.jade.detect.model.Log;
import com.jade.detect.service.DeviceService;
import com.jade.detect.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
@Tag(name = "Dispositivos", description = "Gestión de los dispositivos en el sistema")
public class DeviceController {

    private final DeviceService deviceService;
    private final LogService logService;

    @Autowired
    public DeviceController(DeviceService deviceService, LogService logService) {
        this.deviceService = deviceService;
        this.logService = logService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los dispositivos")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener un dispositivo por ID")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/devicename/{name}")
    @Operation(summary = "Obtener dispositivo por nombre")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Device> getDeviceByName(@PathVariable String name) {
        Optional<Device> device = deviceService.getDeviceByName(name);
        return device.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Obtener dispositivos por ubicación")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<List<Device>> getDevicesByLocation(@PathVariable String location) {
        List<Device> devices = deviceService.getDevicesByLocation(location);
        return devices.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(devices);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo dispositivo")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public Device createDevice(@RequestBody Device device) {
        Device created = deviceService.createDevice(device);
        registrarLog(Log.LogLevel.INFO, "Dispositivo creado: " + created.getName());
        return created;
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un dispositivo por ID")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Optional<Device> updatedDevice = deviceService.updateDevicePartial(id, updates);

        if (updatedDevice.isPresent()) {
            registrarLog(Log.LogLevel.INFO, "Dispositivo actualizado: ID " + id);
            return ResponseEntity.ok(updatedDevice.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un dispositivo")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        Optional<Device> deviceOptional = deviceService.getDeviceById(id);
        if (deviceOptional.isEmpty()) return ResponseEntity.notFound().build();

        registrarLog(Log.LogLevel.WARNING, "Dispositivo eliminado: " + deviceOptional.get().getName());
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }

    // Método privado para registrar logs
    private void registrarLog(Log.LogLevel level, String mensaje) {
        Log log = new Log();
        log.setLevel(level);
        log.setMessage(mensaje);
        logService.saveLog(log);
    }
}
