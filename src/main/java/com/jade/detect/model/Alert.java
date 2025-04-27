package com.jade.detect.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Alert {

    public enum AlertStatus {
        PENDING,
        RESOLVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String level; // Asegúrate de que este campo exista

    @Enumerated(EnumType.STRING)
    private AlertStatus status = AlertStatus.PENDING;

    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructor por defecto
    public Alert() {}

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() { // 👈 ESTE getter es necesario
        return level;
    }

    public void setLevel(String level) { // 👈 También el setter
        this.level = level;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
/////////