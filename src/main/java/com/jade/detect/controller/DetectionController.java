package com.jade.detect.controller;

import com.jade.detect.model.Detection;
import com.jade.detect.service.DetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/detections")
public class DetectionController {
    @Autowired
    private DetectionService detectionService;

    @GetMapping
    public List<Detection> getAllDetections() {
        return detectionService.getAllDetections();
    }

    @PostMapping
    public ResponseEntity<Detection> saveDetection(@RequestBody Detection detection) {
        Detection savedDetection = detectionService.saveDetection(detection);
        return ResponseEntity.ok(savedDetection);
    }
}
