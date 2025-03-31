package com.jade.detect.controller;

import com.jade.detect.model.Detection;
import com.jade.detect.model.Device;
import com.jade.detect.service.DetectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public List<Detection> getAllDetections() {
        return detectionService.getAllDetections();
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
                                                "videoSource": "camara_1",
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
    public ResponseEntity<Detection> saveDetection(@RequestBody Detection detection) {
        Detection savedDetection = detectionService.saveDetection(detection);
        return ResponseEntity.ok(savedDetection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una detección por ID",
            description = "Busca una detección en la base de datos por su Id.")
    public ResponseEntity<Detection> getDetectionById(
            @Parameter(description = "ID de la detección a buscar", example = "1")
            @PathVariable Long id) {
        return detectionService.getDetectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una detección",
            description = "Elimina una detección por su Id si existe en la base de datos.")
    public ResponseEntity<Void> deleteDetection(
            @Parameter(description = "Id de la detección a eliminar", example = "1")
            @PathVariable Long id) {
        detectionService.deleteDetection(id);
        return ResponseEntity.noContent().build();
    }
}
