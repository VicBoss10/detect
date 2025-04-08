package com.jade.detect.repository;

import com.jade.detect.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // Buscar alertas por nivel (opcional, útil para filtrado en el futuro)
    List<Alert> findByLevel(String level);

    // Buscar alertas por estado
    List<Alert> findByStatus(Alert.AlertStatus status);
}
