
package com.jade.detect.model;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now(); // Fecha y hora de la alerta

    @Column(nullable = true, length = 500)
    private String message; // Descripción de la alerta


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.PENDING; // Estado de la alerta

    @ManyToOne
    @JoinColumn(name = "detection_id")
    private Detection detection; // Relación con detección (opcional)

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device; // Relación con un dispositivo (opcional)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Usuario responsable (opcional)



    public enum AlertStatus {
        PENDING, RESOLVED
    }
}