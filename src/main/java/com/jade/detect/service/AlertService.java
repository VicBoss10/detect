package com.jade.detect.service;

import com.jade.detect.model.Alert;
import com.jade.detect.model.Alert.AlertStatus;
import com.jade.detect.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public Optional<Alert> getAlertById(Long id) {
        return alertRepository.findById(id);
    }

    public Alert saveAlert(Alert alert) {
        alert.setStatus(AlertStatus.PENDING);
        return alertRepository.save(alert);
    }

    public void resolveAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada"));
        alert.setStatus(AlertStatus.RESOLVED);
        alertRepository.save(alert);
    }

    public Optional<Alert> updateAlert(Long id, Alert partialAlert) {
        return alertRepository.findById(id).map(existing -> {
            if (partialAlert.getMessage() != null) {
                existing.setMessage(partialAlert.getMessage());
            }
            if (partialAlert.getLevel() != null) {
                existing.setLevel(partialAlert.getLevel());
            }
            if (partialAlert.getStatus() != null) {
                existing.setStatus(partialAlert.getStatus());
            }
            return alertRepository.save(existing);
        });
    }

    public void deleteAlert(Long id) {
        if (!alertRepository.existsById(id)) {
            throw new RuntimeException("Alerta no encontrada");
        }
        alertRepository.deleteById(id);
    }
}
////////