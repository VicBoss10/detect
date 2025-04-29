package com.jade.detect.controller;

import com.jade.detect.model.Alert;
import com.jade.detect.model.Alert.AlertStatus;
import com.jade.detect.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Alertas", description = "Gestión de las alertas en el sistema")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las alertas", description = "Devuelve la lista de alertas registradas.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una alerta por ID", description = "Recupera una alerta específica por su ID.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Alert> getAlertById(
            @Parameter(description = "ID de la alerta", example = "1") @PathVariable Long id) {
        Optional<Alert> alert = alertService.getAlertById(id);
        return alert.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Registrar una nueva alerta",
            description = "Crea una nueva alerta en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la alerta a registrar",
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
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.saveAlert(alert);
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolver una alerta", description = "Marca una alerta como resuelta por su ID.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Void> resolveAlert(
            @Parameter(description = "ID de la alerta", example = "1") @PathVariable Long id) {
        alertService.resolveAlert(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar una alerta parcialmente", description = "Permite actualizar uno o más campos de la alerta por su ID.")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Alert> updateAlert(
            @Parameter(description = "ID de la alerta a actualizar", example = "1") @PathVariable Long id,
            @RequestBody Alert partialAlert) {
        Optional<Alert> updatedAlert = alertService.updateAlert(id, partialAlert);
        return updatedAlert.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una alerta", description = "Elimina una alerta del sistema por su ID.")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Void> deleteAlert(
            @Parameter(description = "ID de la alerta a eliminar", example = "1") @PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }
}