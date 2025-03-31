package com.jade.detect.controller;

import com.jade.detect.model.Device;
import com.jade.detect.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un dispositivo por ID",
            description = "Busca un dispositivo en la base de datos por su ID.")
    public ResponseEntity<Device> getDeviceById(
            @Parameter(description = "ID del dispositivo a buscar", example = "1")
            @PathVariable Long id) {
        return deviceService.getDeviceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
                                          "name": "Cámara de seguridad",
                                          "type": "Cámara IP",
                                          "location": "Oficina principal",
                                          "status": "ACTIVE"
                                        }
                                        """
                            )
                    )
            )
    )
    public Device createDevice(@RequestBody Device device) {
        return deviceService.createDevice(device);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un dispositivo",
            description = "Elimina un dispositivo por su ID si existe en la base de datos.")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID del dispositivo a eliminar", example = "1")
            @PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
