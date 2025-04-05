
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
    private Long alert_id;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now(); // Fecha y hora de la alerta

    @Column(nullable = true, length = 500)
    private String message; // Descripción de la alerta


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status = AlertStatus.PENDING; // Estado de la alerta

    @ManyToOne
    @JoinColumn(name = "detection_id")
    private Long detection_id; // Relación con detección (opcional)

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Long device_id; // Relación con un dispositivo (opcional)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long user_id; // Usuario responsable (opcional)



    public enum AlertStatus {
        PENDING, RESOLVED
    }
}