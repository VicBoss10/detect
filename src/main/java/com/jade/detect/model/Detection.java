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
    private Long detection_id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @JsonManagedReference // Esta es la clave para romper el ciclo
    @OneToMany(mappedBy = "detection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetectedObject> detectedObjects = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        date = LocalDateTime.now();
    }

    // Método helper para mantener la relación bidireccional
    public void addDetectedObject(DetectedObject object) {
        detectedObjects.add(object);
        object.setDetection(this);
    }
}