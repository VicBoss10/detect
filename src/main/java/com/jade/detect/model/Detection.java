package com.jade.detect.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "detections")
public class Detection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String videoSource;

    private LocalDateTime timestamp;

    @JsonManagedReference // Esta es la clave para romper el ciclo
    @OneToMany(mappedBy = "detection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetectedObject> detectedObjects = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Método helper para mantener la relación bidireccional
    public void addDetectedObject(DetectedObject object) {
        detectedObjects.add(object);
        object.setDetection(this);
    }
}