package com.jade.detect.controller;

import com.jade.detect.model.Detection;
import com.jade.detect.model.Device;
import com.jade.detect.service.DetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detections")
@Tag(name = "Detecciones", description = "Gestión de las detecciones, en esta tabla también se gestiona la clase DetectedObject")
public class DetectionController {
    @Autowired
    private DetectionService detectionService;

    @GetMapping
    @Operation(summary = "Obtener las detecciones",
            description = "Devuelve una lista con todas las detecciones registrados y los objetos detectados en cada detección.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<List<Detection>> getAllDetections() {
        List<Detection> detections = detectionService.getAllDetections();
        if (detections.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(detections, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva detección",
            description = "Crea una nueva detección y registra los objetos detectados en ella junto con sus coordenadas en la imagen.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Detección a registrar.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de detección",
                                    value = """
                                            {
                                                "device": { "id": 1 },                                        
                                                "detectedObjects": [
                                                    {
                                                        "label": "Persona",
                                                        "confidence": 95.2,
                                                        "x1": 220,
                                                        "y1": 80,
                                                        "x2": 300,
                                                        "y2": 400
                                                    },
                                                    {
                                                        "label": "Auto",
                                                        "confidence": 98.5,
                                                        "x1": 50,
                                                        "y1": 100,
                                                        "x2": 200,
                                                        "y2": 300
                                                    }
                                                ]
                                            }
                                        """
                            )
                    )
            )
    )
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Detection> saveDetection(@RequestBody Detection detection) {
        try {
            Detection savedDetection = detectionService.saveDetection(detection);
            return new ResponseEntity<>(savedDetection, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una detección por ID",
            description = "Busca una detección en la base de datos por su Id.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Detection> getDetectionById(
            @Parameter(description = "ID de la detección a buscar", example = "1")
            @PathVariable Long id) {
        return detectionService.getDetectionById(id)
                .map(detection -> new ResponseEntity<>(detection, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una detección",
            description = "Elimina una detección por su Id si existe en la base de datos.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Void> deleteDetection(
            @Parameter(description = "Id de la detección a eliminar", example = "1")
            @PathVariable Long id) {
        try {
            if (detectionService.getDetectionById(id).isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            detectionService.deleteDetection(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/device/{deviceId}")
    @Operation(summary = "Obtener detecciones por ID de dispositivo",
            description = "Devuelve todas las detecciones realizadas por un dispositivo específico.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<List<Detection>> getDetectionsByDeviceId(
            @Parameter(description = "ID del dispositivo", example = "1")
            @PathVariable Long deviceId) {
        List<Detection> detections = detectionService.getDetectionsByDeviceId(deviceId);
        if (detections.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(detections, HttpStatus.OK);
    }
}