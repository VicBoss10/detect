package com.jade.detect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jade.detect.repository.DetectedObjectRepository;
import com.jade.detect.model.DetectedObject;

import java.util.List;
import java.util.Optional;

@Service
public class DetectedObjectService {

    @Autowired
    private DetectedObjectRepository detectedObjectRepository;

    // Obtener todos los objetos detectados
    public List<DetectedObject> getAllDetectedObjects() {
        return detectedObjectRepository.findAll();
    }

    // Guardar un objeto detectado
    public DetectedObject saveDetectedObject(DetectedObject detectedObject) {
        return detectedObjectRepository.save(detectedObject);
    }

    // Buscar un objeto detectado por ID
    public Optional<DetectedObject> getDetectedObjectById(Long id) {
        return detectedObjectRepository.findById(id);
    }

    // Eliminar un objeto detectado por ID
    public void deleteDetectedObject(Long id) {
        detectedObjectRepository.deleteById(id);
    }
}
