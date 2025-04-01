package com.jade.detect.controller;

import com.jade.detect.model.Alert;
import com.jade.detect.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.saveAlert(alert);
    }

    @PutMapping("/{id}/resolve")
    public void resolveAlert(@PathVariable Long id) {
        alertService.resolveAlert(id);
    }
}