package com.jade.detect.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "detected_objects")
public class DetectedObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference // Esta es la clave para romper el ciclo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detection_id")
    private Detection detection;

    private String label;
    private float confidence;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
}