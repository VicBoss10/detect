package com.jade.detect.controller;

import com.jade.detect.model.Log;
import com.jade.detect.service.LogService;
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
@RequestMapping("/logs")
@Tag(name = "Logs", description = "Gestión de registros de logs del sistema")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los logs", description = "Devuelve una lista con todos los logs registrados en el sistema.")
    public List<Log> getAllLogs() {
        return logService.getAllLogs();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un log por ID", description = "Busca un log en la base de datos por su ID.")
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Log> getLogById(
            @Parameter(description = "ID del log a buscar", example = "1")
            @PathVariable Long id) {
        Optional<Log> log = logService.getLogById(id);
        return log.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo log", description = "Crea un nuevo registro de log en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Log a registrar",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Ejemplo de log",
                                    value = """
                                        {
                                          "level": "INFO",
                                          "message": "El sistema ha iniciado correctamente",
                                          "device": {
                                       
                                          }
                                        }
                                        """
                            )
                    )
            )
    )
    @PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
    public ResponseEntity<Log> createLog(@RequestBody Log log) {
        Log savedLog = logService.saveLog(log);
        return ResponseEntity.ok(savedLog);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un log", description = "Elimina un log por su ID si existe en la base de datos.")
    @PreAuthorize("hasRole('admin_client_role')")
    public ResponseEntity<Void> deleteLog(
            @Parameter(description = "ID del log a eliminar", example = "1")
            @PathVariable Long id) {
        logService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}