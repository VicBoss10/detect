package com.jade.detect.controller;

import com.jade.detect.model.Alert;
import com.jade.detect.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Alertas", description = "Gestión de las alertas en el sistema")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(
            summary = "Obtener todas las alertas",
            description = "Devuelve una lista con todas las alertas registradas en el sistema."
    )
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @PostMapping
    @Operation(
            summary = "Registrar una nueva alerta",
            description = "Crea una nueva alerta en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Alerta a registrar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de alerta",
                                    value = """
                                            {
                                                "level": "INFO",
                                                "message": "El sistema ha iniciado correctamente"
                                            }
                                        """
                            )
                    )
            )
    )
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.saveAlert(alert);
    }

    @PutMapping("/{id}/resolve")
    @Operation(
            summary = "Resolver una alerta",
            description = "Marca la alerta especificada como resuelta por su ID."
    )
    public void resolveAlert(@Parameter(description = "ID de la alerta a resolver", example = "1")
                             @PathVariable Long id) {
        alertService.resolveAlert(id);
    }
}
