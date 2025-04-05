package com.jade.detect.service;

import com.jade.detect.model.Alert;
import com.jade.detect.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Alert saveAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    public void createAlert(  String message) {
        Alert alert = new Alert();
        alert.setMessage(message);
        alertRepository.save(alert);
    }

    public void resolveAlert(Long id) {
        Alert alert = alertRepository.findById(id).orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        alert.setStatus(Alert.AlertStatus.RESOLVED);
        alertRepository.save(alert);
    }
}