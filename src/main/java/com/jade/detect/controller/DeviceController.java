package com.jade.detect.controller;

import com.jade.detect.model.Device;
import com.jade.detect.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los dispositivos",
            description = "Devuelve una lista con todos los dispositivos registrados en el sistema.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener un dispositivo por ID",
            description = "Busca un dispositivo en la base de datos por su ID.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Device> getDeviceById(
            @Parameter(description = "ID del dispositivo a buscar", example = "1")
            @PathVariable Long id) {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/devicename/{name}")
    @Operation(summary = "Obtener dispositivo por nombre",
            description = "Busca un dispositivo en la base de datos por su nombre.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Device> getDeviceByName(
            @Parameter(description = "Nombre del dispositivo a buscar", example = "Camara de seguridad")
            @PathVariable String name) {
        Optional<Device> device = deviceService.getDeviceByName(name);
        return device.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/location/{location}")
    @Operation(summary = "Obtener dispositivos por ubicación",
            description = "Busca todos los dispositivos en la base de datos que tengan la ubicación especificada.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<List<Device>> getDevicesByLocation(
            @Parameter(description = "Ubicación del dispositivo", example = "Pasto")
            @PathVariable String location) {
        List<Device> devices = deviceService.getDevicesByLocation(location);
        if (devices.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(devices);
        }
    }


    @PostMapping
    @Operation(summary = "Registrar un nuevo dispositivo",
            description = "Crea un nuevo dispositivo en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dispositivo a registrar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de dispositivo",
                                    value = """
                                        {
                                          "name": "Camara de seguridad",
                                          "type": "Cámara IP",
                                          "location": "Oficina principal",
                                          "status": "ACTIVE"
                                        }
                                        """
                            )
                    )
            )
    )
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public Device createDevice(@RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un dispositivo por ID",
            description = "Actualiza uno o varios campos de un dispositivo dado su ID.")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Device> updateDevice(
            @Parameter(description = "ID del dispositivo a actualizar", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos del dispositivo a actualizar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Actualizar estado y ubicación",
                                    value = """
                            {
                              "location": "Pasto",
                              "status": "INACTIVE"
                            }
                            """
                            )
                    )
            )
            @RequestBody Map<String, Object> updates) {
        Optional<Device> updatedDevice = deviceService.updateDevicePartial(id, updates);
        return updatedDevice.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un dispositivo",
            description = "Elimina un dispositivo por su ID si existe en la base de datos.")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID del dispositivo a eliminar", example = "1")
            @PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
