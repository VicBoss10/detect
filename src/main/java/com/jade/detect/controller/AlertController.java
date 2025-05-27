package com.jade.detect.controller;

import com.jade.detect.model.Alert;
import com.jade.detect.model.Log;
import com.jade.detect.service.AlertService;
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
import java.util.Optional;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Alertas", description = "Gestión de las alertas en el sistema")
public class AlertController {

    private final AlertService alertService;
    private final LogService logService;

    @Autowired
    public AlertController(AlertService alertService, LogService logService) {
        this.alertService = alertService;
        this.logService = logService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las alertas")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una alerta por ID")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Alert> getAlertById(@Parameter(description = "ID de la alerta", example = "1") @PathVariable Long id) {
        Optional<Alert> alert = alertService.getAlertById(id);
        if (alert.isPresent()) {
            return ResponseEntity.ok(alert.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            summary = "Registrar una nueva alerta",
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
        Alert creada = alertService.saveAlert(alert);
        registrarLog(Log.LogLevel.INFO, "Se creó una alerta con ID: " + creada.getId());
        return creada;
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolver una alerta")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Void> resolveAlert(@Parameter(description = "ID de la alerta", example = "1") @PathVariable Long id) {
        alertService.resolveAlert(id);
        registrarLog(Log.LogLevel.INFO, "Se resolvió la alerta con ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente una alerta")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Alert> updateAlert(@Parameter(description = "ID de la alerta", example = "1") @PathVariable Long id,
                                             @RequestBody Alert partialAlert) {
        Optional<Alert> updatedAlert = alertService.updateAlert(id, partialAlert);
        if (updatedAlert.isPresent()) {
            registrarLog(Log.LogLevel.INFO, "Se actualizó la alerta con ID: " + id);
            return ResponseEntity.ok(updatedAlert.get());
        } else {
            registrarLog(Log.LogLevel.WARNING, "No se encontró alerta con ID: " + id + " para actualizar.");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una alerta")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Void> deleteAlert(@Parameter(description = "ID de la alerta", example = "1") @PathVariable Long id) {
        Optional<Alert> alert = alertService.getAlertById(id);
        if (alert.isEmpty()) {
            registrarLog(Log.LogLevel.ERROR, "No se encontró la alerta con ID: " + id + " para eliminar.");
            return ResponseEntity.notFound().build();
        }
        registrarLog(Log.LogLevel.WARNING, "Se eliminó la alerta con ID: " + id);
        alertService.deleteAlert(id);
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
