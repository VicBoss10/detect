package com.jade.detect.service;

import com.jade.detect.model.DetectedObject;
import com.jade.detect.model.Detection;
import com.jade.detect.repository.DetectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DetectionService {
    @Autowired
    private DetectionRepository detectionRepository;

    public List<Detection> getAllDetections() {
        return detectionRepository.findAll();
    }

    @Transactional
    public Detection saveDetection(Detection detection) {
        // Si no viene con timestamp, se usará el creado por @PrePersist

        // Establecer la referencia bidireccional
        if (detection.getDetectedObjects() != null) {
            for (DetectedObject obj : detection.getDetectedObjects()) {
                obj.setDetection(detection);
            }
        }

        return detectionRepository.save(detection);
    }

//    public Detection saveDetection(Detection detection) {
//        return detectionRepository.save(detection);
//    }
}