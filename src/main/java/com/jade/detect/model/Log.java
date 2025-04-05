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
    private Long log_id;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now(); // Guarda la fecha y hora del log

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private LogLevel level; // Nivel del log (INFO, WARNING, ERROR)

    @Column(nullable = true, length = 500)
    private String message; // Descripción del evento

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private Long user_id; // Usuario que generó el evento

    @ManyToOne
    @JoinColumn(nullable = false, name = "device_id")
    private Long device_id; // Dispositivo relacionado

    public enum LogLevel {
        INFO, WARNING, ERROR
    }
}