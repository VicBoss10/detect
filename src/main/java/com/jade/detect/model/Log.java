package com.jade.detect.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now(); // Guarda la fecha y hora del log

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogLevel level; // Nivel del log (INFO, WARNING, ERROR)

    @Column(nullable = false, length = 500)
    private String message; // Descripción del evento

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Usuario que generó el evento (opcional)

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device; // Dispositivo relacionado (opcional)

    public enum LogLevel {
        INFO, WARNING, ERROR
    }
}